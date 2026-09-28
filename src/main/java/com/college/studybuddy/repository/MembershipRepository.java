
package com.college.studybuddy.repository;

import com.college.studybuddy.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    boolean existsByStudentIdAndStudyGroupId(Long studentId, Long groupId);

    List<Membership> findByStudyGroupId(Long groupId);

    long countByStudyGroupId(Long groupId);

    List<Membership> findByStudentId(Long studentId);

    Optional<Membership> findByStudentIdAndStudyGroupId(Long studentId, Long groupId);

    List<Membership> findByStudyGroupIdIn(List<Long> groupIds);
}