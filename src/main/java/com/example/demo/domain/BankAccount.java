package com.example.demo.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "bank_accounts")
public class BankAccount {

  @Id
  @GeneratedValue
  private Long id;

  private String prefix;
  private String suffix;

  private boolean applyForLoan;

  private BigDecimal balance;

  @ManyToOne
  @JoinColumn(name = "subject")
  private Subject subject;
}
