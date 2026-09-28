package com.college.studybuddy.service;

import com.college.studybuddy.entity.Membership;
import com.college.studybuddy.entity.Student;
import com.college.studybuddy.entity.StudyGroup;
import com.college.studybuddy.entity.Subject;
import com.college.studybuddy.repository.MembershipRepository;
import com.college.studybuddy.repository.StudentRepository;
import com.college.studybuddy.repository.StudyGroupRepository;
import com.college.studybuddy.repository.SubjectRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final StudentRepository studentRepository;
    private final MembershipRepository membershipRepository;
    private final SubjectRepository subjectRepository;

    public StudyGroupService(
            StudyGroupRepository studyGroupRepository,
            StudentRepository studentRepository,
            MembershipRepository membershipRepository,
            SubjectRepository subjectRepository) {

        this.studyGroupRepository = studyGroupRepository;
        this.studentRepository = studentRepository;
        this.membershipRepository = membershipRepository;
        this.subjectRepository = subjectRepository;
    }

    public ResponseEntity<Map<String, String>> joinGroup(Long groupId, Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Student not found"));
        }
        StudyGroup group = studyGroupRepository.findById(groupId).orElse(null);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Study group not found"));
        }
        if (membershipRepository.existsByStudentIdAndStudyGroupId(studentId, groupId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Student is already a member of this group"));
        }
        long currentMembers = membershipRepository.countByStudyGroupId(groupId);
        if (currentMembers >= group.getMaxMembers()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Study group is full"));
        }
        Student student = studentRepository.findById(studentId).get();
        Membership membership = new Membership();
        membership.setStudent(student);
        membership.setStudyGroup(group);
        membershipRepository.save(membership);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Student joined the study group successfully"));
    }

    public List<Membership> getMembers(Long groupId) {
        return membershipRepository.findByStudyGroupId(groupId);
    }

    public long getMemberCount(Long groupId) {
        return membershipRepository.countByStudyGroupId(groupId);
    }

    public ResponseEntity<Map<String, String>> removeMember(Long groupId, Long studentId) {
        return membershipRepository.findByStudentIdAndStudyGroupId(studentId, groupId)
                .map(m -> {
                    membershipRepository.delete(m);
                    return ResponseEntity.ok(Map.of("message", "Member removed successfully"));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Membership not found")));
    }

    public ResponseEntity<?> updateGroup(Long id, StudyGroup updated, Long subjectId) {
        StudyGroup group = studyGroupRepository.findById(id).orElse(null);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Study group not found"));
        }
        long currentCount = membershipRepository.countByStudyGroupId(id);
        if (updated.getMaxMembers() < currentCount) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Maximum members cannot be less than current member count"));
        }
        if (subjectId != null) {
            Subject subject = subjectRepository.findById(subjectId).orElse(null);
            if (subject == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Subject not found"));
            }
            group.setSubject(subject);
        }
        group.setName(updated.getName());
        group.setMaxMembers(updated.getMaxMembers());
        return ResponseEntity.ok(studyGroupRepository.save(group));
    }

    public ResponseEntity<Map<String, String>> deleteGroup(Long id) {
        if (!studyGroupRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Study group not found"));
        }
        membershipRepository.deleteAll(membershipRepository.findByStudyGroupId(id));
        studyGroupRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Study group deleted successfully"));
    }
}
