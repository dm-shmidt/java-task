package com.example.demo.repository;

import com.example.demo.domain.Subject;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SubjectRepository extends JpaRepository<Subject, Long>, JpaSpecificationExecutor<Subject> {


  @Query("SELECT ba.subject FROM BankAccount ba WHERE ba.balance < 60")
  List<Subject> getSubjectsWithLowBalance();
}
