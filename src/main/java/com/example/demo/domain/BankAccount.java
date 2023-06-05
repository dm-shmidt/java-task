package com.example.demo.domain;

import java.math.BigDecimal;
import javax.persistence.*;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Transactional
@Entity
@Table(name = "bank_accounts")
public class BankAccount {

  @Id
  @GeneratedValue
  private Long id;

  private String accountNumber;
  private String prefix;
  private String suffix;

  private boolean applyForLoan;

  private BigDecimal balance;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "subject")
  private Subject subject;
}
