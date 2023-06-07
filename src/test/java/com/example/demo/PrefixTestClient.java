package com.example.demo;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "prefix-test-client", url = "${prefix-test-client.url}", decode404 = true)
public interface PrefixTestClient {

    @GetMapping("/prefix")
    ResponseEntity<Prefix> getPrefix();

    @Data
    class Prefix {
        private String prefix;
    }
}
