package com.example.demo.service;

import com.example.demo.api.CreateSubjectRequest;
import com.example.demo.api.SubjectResponse;
import com.example.demo.client.PrefixClient;
import com.example.demo.mapper.SubjectMapper;
import com.example.demo.repository.BankAccountRepository;
import com.example.demo.repository.SubjectRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class SubjectServiceImpl implements SubjectService {

  private final SubjectMapper subjectMapper;
  private final SubjectRepository subjectRepository;
  private final BankAccountRepository bankAccountRepository;
  private final PrefixClient prefixClient;
  private BankAccountService bankAccountService;

  @Override
  @Transactional
  public Long save(CreateSubjectRequest request) {
    return subjectRepository.saveAndFlush(subjectMapper.map(request)).getId();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<SubjectResponse> findById(Long id) {
    return subjectRepository.findById(id)
        .map(db -> {
          final var mapped = subjectMapper.map(db);
          mapped.setNumberOfAccounts(bankAccountRepository.numberOfAccounts(db.getId()));

          return mapped;
        });
  }

  @Override
  @Transactional(readOnly = true)
  public List<SubjectResponse> subjectsWithLowBalance() {
    return subjectRepository.getSubjectsWithLowBalance()
        .stream()
        .map(subjectMapper::map)
        .collect(Collectors.toList());
  }
}
