package com.example.demo.mapper;

import com.example.demo.api.CreateSubjectRequest;
import com.example.demo.api.SubjectResponse;
import com.example.demo.domain.Subject;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = BankAccountMapper.class)
public interface SubjectMapper {

  Subject map(CreateSubjectRequest request);

  @Named("toDto")
  SubjectResponse map(Subject subject);

  @IterableMapping(qualifiedByName = "toDto")
  List<SubjectResponse> map(List<Subject> subjectList);
}
