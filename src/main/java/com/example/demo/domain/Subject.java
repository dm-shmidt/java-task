package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Transactional
@Entity
@Table(name = "subjects")
public class Subject {

  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private String lastName;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "subject", referencedColumnName = "id")
  private List<BankAccount> accounts = new ArrayList<>();

}
