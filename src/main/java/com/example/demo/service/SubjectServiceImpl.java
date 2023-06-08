package com.example.demo.service;

import com.example.demo.api.CreateSubjectRequest;
import com.example.demo.api.SubjectResponse;
import com.example.demo.domain.Subject;
import com.example.demo.mapper.SubjectMapper;
import com.example.demo.repository.SubjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class SubjectServiceImpl implements SubjectService {

    private final SubjectMapper subjectMapper;
    private final SubjectRepository subjectRepository;
    private final BankAccountService bankAccountService;

    @Override
    @Transactional
    public Long save(CreateSubjectRequest request) {
        request.getAccounts().forEach(bankAccountService::enrichAccountBeforeSave);
        Subject subject = subjectMapper.map(request);
        return subjectRepository.saveAndFlush(subject).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubjectResponse> findById(Long id) {
        return subjectRepository.findById(id)
                .map(db -> {
                    final var mapped = subjectMapper.map(db);
                    mapped.setNumberOfAccounts(bankAccountService.countBySubjectId(db.getId()));

                    return mapped;
                });
    }

    @Override
    public List<SubjectResponse> findAll() {
        return subjectMapper.map(subjectRepository.findAll()).stream()
                .peek(mapped -> mapped.setNumberOfAccounts(bankAccountService.countBySubjectId(mapped.getId())))
                .toList();
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
