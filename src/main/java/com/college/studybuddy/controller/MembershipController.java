package com.college.studybuddy.controller;

import com.college.studybuddy.entity.Membership;
import com.college.studybuddy.service.StudyGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/groups")
public class MembershipController {

    private final StudyGroupService studyGroupService;

    public MembershipController(StudyGroupService studyGroupService) {
        this.studyGroupService = studyGroupService;
    }

    @PostMapping("/{groupId}/join/{studentId}")
    public ResponseEntity<Map<String, String>> joinGroup(
            @PathVariable Long groupId,
            @PathVariable Long studentId) {
        return studyGroupService.joinGroup(groupId, studentId);
    }

    @GetMapping("/{groupId}/members")
    public List<Membership> getMembers(@PathVariable Long groupId) {
        return studyGroupService.getMembers(groupId);
    }

    @GetMapping("/{groupId}/count")
    public long getMemberCount(@PathVariable Long groupId) {
        return studyGroupService.getMemberCount(groupId);
    }

    @DeleteMapping("/{groupId}/members/{studentId}")
    public ResponseEntity<Map<String, String>> removeMember(
            @PathVariable Long groupId,
            @PathVariable Long studentId) {
        return studyGroupService.removeMember(groupId, studentId);
    }
}
