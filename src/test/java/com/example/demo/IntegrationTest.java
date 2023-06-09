package com.example.demo;

import com.example.demo.client.PrefixClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureWireMock(port = 8082, stubs = "classpath:/stubs")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(FeignClientsConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PrefixClient prefixClient;
    @Autowired
    private PrefixTestClient prefixTestClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void feignDemoSuccess() {
        ResponseEntity<PrefixClient.Prefix> response = prefixClient.getPrefix();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String prefix = Objects.requireNonNull(response.getBody()).getPrefix();
        assertEquals("0300-4", prefix);
    }

    @Test
    void feignDemoBadRequest() {
        AtomicReference<ResponseEntity<PrefixTestClient.Prefix>> prefix = new AtomicReference<>();
        FeignException.InternalServerError error = assertThrows(FeignException.InternalServerError.class, () -> prefix.set(prefixTestClient.getPrefix()));
        assertEquals(FeignException.InternalServerError.class, error.getClass());
        assertEquals(500, error.status());
        assertNull(prefix.get());
    }

    @Test
    @Order(1)
    void testAddSubjectAndBankAccount() throws Exception {
        String uri = "/subjects";
        String content = """
                {
                    "name": "Jane",
                    "lastName": "Thomson",
                    "accounts": [
                        {
                            "prefix": "pre-",
                            "suffix": "-suffix",
                            "balance": 500
                        },
                        {
                            "prefix": "pre1-",
                            "suffix": "-suffix1",
                            "balance": 500
                        }
                    ]
                }""";

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @Order(2)
    void testAddTransaction_MultipleCases() throws Exception {
        String uri = "/accounts?page=0&size=10&sort=id";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode responseJson = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        long testAccountId = responseJson.findValue("id").asLong();
        assertEquals(2, responseJson.findValues("id").size());
        assertNotEquals(-1L, testAccountId);

        uri = "/accounts/" + testAccountId + "/transaction";
        String content = """
                {
                    "amount": -300
                }
                """;

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isAccepted())
                .andReturn();

        responseJson = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        assertNotNull(responseJson.findValue("transaction_id"));
        BigDecimal resultBalance = new BigDecimal(String.valueOf(responseJson.findValue("result_balance")));

        assertEquals(new BigDecimal("200.0"), resultBalance);
        assertNull(responseJson.findValue("error"));

        //---------------------------------------------------------------------------------------------
        //---- test the same account: Add valid transaction and balance becomes lower than threshold
        //---------------------------------------------------------------------------------------------

        uri = "/accounts/" + testAccountId + "/transaction";
        content = """
                {
                    "amount": -10
                }
                """;

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isAccepted())
                .andReturn();

        responseJson = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        assertNotNull(responseJson.findValue("transaction_id"));

        resultBalance = new BigDecimal(String.valueOf(responseJson.findValue("result_balance")));

        assertEquals(new BigDecimal("190.0"), resultBalance);
        String error = String.valueOf(responseJson.findValue("error"));
        assertTrue(error.contains("Balance is under threshold"));

        //---------------------------------------------------------------------------------------------
        //---- test the same account: Add transaction with an amount exceeding the current balance.
        //---------------------------------------------------------------------------------------------

        content = """
                {
                    "amount": -200
                }
                """;

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        responseJson = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        assertNotNull(responseJson.findValue("error"));
        error = String.valueOf(responseJson.findValue("error"));
        assertTrue(error.contains("Transaction amount exceeds the balance"));

    }
}
