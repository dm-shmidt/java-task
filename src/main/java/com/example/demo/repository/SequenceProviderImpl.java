package com.example.demo.repository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Getter
@RequiredArgsConstructor
public class SequenceProviderImpl implements SequenceProvider {

  private final short SUFFIX_LENGTH = 9; // 9 makes sense to keep suffix not cycled one.

  private final static String QUERY = "select nextval('account_sequence')";

  private final JdbcTemplate jdbcTemplate;

  @Override
  @Transactional(readOnly = true)
  public String next() {
    return String.format("%0" + SUFFIX_LENGTH + "d", jdbcTemplate.queryForObject(QUERY, Long.class));
  }
}
