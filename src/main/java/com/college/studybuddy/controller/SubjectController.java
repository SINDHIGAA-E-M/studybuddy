package com.college.studybuddy.controller;

import com.college.studybuddy.entity.Subject;
import com.college.studybuddy.repository.StudyGroupRepository;
import com.college.studybuddy.repository.SubjectRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final StudyGroupRepository studyGroupRepository;

    public SubjectController(SubjectRepository subjectRepository, StudyGroupRepository studyGroupRepository) {
        this.subjectRepository = subjectRepository;
        this.studyGroupRepository = studyGroupRepository;
    }

    @PostMapping
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody Subject subject) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectRepository.save(subject));
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        return subjectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubject(@PathVariable Long id, @Valid @RequestBody Subject updated) {
        return subjectRepository.findById(id).map(subject -> {
            subject.setName(updated.getName());
            return ResponseEntity.ok(subjectRepository.save(subject));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSubject(@PathVariable Long id) {
        if (!subjectRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Subject not found"));
        }
        if (studyGroupRepository.existsBySubjectId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Cannot delete subject because study groups are associated with it"));
        }
        subjectRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Subject deleted successfully"));
    }
}
