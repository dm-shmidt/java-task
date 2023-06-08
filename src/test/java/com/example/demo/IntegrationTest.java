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
                            "balance": 700
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
    void testAddValidTransaction() throws Exception {
        String uri = "/accounts?page=0&size=10&sort=id";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode responseJson = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        Long accountId = responseJson.findValue("id").asLong();
        assertNotEquals(-1L, accountId);

        uri = "/accounts/" + accountId + "/transaction";
        String content = """
                {
                    "amount": 234
                }
                """;

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isAccepted())
                .andReturn();

        responseJson = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        Long transactionId = responseJson.findValue("id").asLong();

        System.out.println(transactionId);
    }
    @Test
    @Order(3)
    void testAddValidTransactionAndBalanceIsLowerThanBalanceThreshold200() throws Exception {
        String uri = "/accounts?page=0&size=10&sort=id";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode responseJson = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        Long accountId = responseJson.findValue("id").asLong();
        assertNotEquals(-1L, accountId);

        uri = "/accounts/" + accountId + "/transaction";
        String content = """
                {
                    "amount": 234
                }
                """;

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isAccepted())
                .andReturn();

        responseJson = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        Long transactionId = responseJson.findValue("id").asLong();

        System.out.println(transactionId);
    }


}
