package com.example.demo.resource;

import com.example.demo.api.CreateSubjectRequest;
import com.example.demo.api.SubjectResponse;
import com.example.demo.service.SubjectService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SubjectResource {

  private final SubjectService subjectService;

  @PostMapping("/subjects")
  public ResponseEntity<Void> create(@RequestBody CreateSubjectRequest request) {
    return ResponseEntity.created(URI.create("http://localhost:8080/subjects/" + subjectService.save(request))).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<SubjectResponse> findById(@PathVariable Long id) {
    final var result = subjectService.findById(id);

    if (result.isEmpty()) {
      return ResponseEntity.badRequest().build();
    } else {
      return ResponseEntity.ok(result.get());
    }
  }
}
