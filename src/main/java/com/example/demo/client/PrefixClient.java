package com.example.demo.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "prefix-client", url = "${prefix-client.url}", decode404 = true)
public interface PrefixClient {

  @GetMapping("/prefix")
  Prefix getPrefix();

  @Data
  class Prefix {
    private String prefix;
  }
}
