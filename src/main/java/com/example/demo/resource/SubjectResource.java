package com.example.demo.resource;

import com.example.demo.api.CreateSubjectRequest;
import com.example.demo.api.SubjectResponse;
import com.example.demo.service.SubjectService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subjects")
public class SubjectResource {

  private final SubjectService subjectService;

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody CreateSubjectRequest request) {
    return ResponseEntity.created(URI.create("http://localhost:8080/subjects/" + subjectService.save(request))).build();
  }

  @GetMapping
  public ResponseEntity<?> allSubjects() {
    return ResponseEntity.ok(subjectService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<SubjectResponse> findById(@PathVariable Long id) {
    final var result = subjectService.findById(id);

    return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}
