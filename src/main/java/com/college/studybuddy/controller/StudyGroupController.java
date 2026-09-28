package com.college.studybuddy.controller;

import com.college.studybuddy.entity.StudyGroup;
import com.college.studybuddy.entity.Subject;
import com.college.studybuddy.repository.StudyGroupRepository;
import com.college.studybuddy.repository.SubjectRepository;
import com.college.studybuddy.service.StudyGroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/groups")
public class StudyGroupController {

    private final StudyGroupRepository studyGroupRepository;
    private final SubjectRepository subjectRepository;
    private final StudyGroupService studyGroupService;

    public StudyGroupController(
            StudyGroupRepository studyGroupRepository,
            SubjectRepository subjectRepository,
            StudyGroupService studyGroupService) {
        this.studyGroupRepository = studyGroupRepository;
        this.subjectRepository = subjectRepository;
        this.studyGroupService = studyGroupService;
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestParam Long subjectId, @Valid @RequestBody StudyGroup group) {
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Subject not found"));
        }
        group.setSubject(subject);
        return ResponseEntity.status(HttpStatus.CREATED).body(studyGroupRepository.save(group));
    }

    @GetMapping
    public List<StudyGroup> getAllGroups() {
        return studyGroupRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable Long id) {
        return studyGroupRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/subject/{subjectId}")
    public List<StudyGroup> getGroupsBySubject(@PathVariable Long subjectId) {
        return studyGroupRepository.findBySubjectId(subjectId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(
            @PathVariable Long id,
            @RequestParam(required = false) Long subjectId,
            @Valid @RequestBody StudyGroup updated) {
        return studyGroupService.updateGroup(id, updated, subjectId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteGroup(@PathVariable Long id) {
        return studyGroupService.deleteGroup(id);
    }
}
