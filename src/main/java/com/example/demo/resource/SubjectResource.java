package com.example.demo.resource;

import com.example.demo.api.CreateSubjectRequest;
import com.example.demo.api.SubjectResponse;
import com.example.demo.service.SubjectService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subjects")
public class SubjectResource {

  private final SubjectService subjectService;

  // Generated uri-string avoiding constant value of it.
  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreateSubjectRequest request) {
    String location = ServletUriComponentsBuilder
            .fromCurrentRequest().buildAndExpand().toUriString() + "/";
    return ResponseEntity.created(URI.create(location + subjectService.save(request))).build();
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
