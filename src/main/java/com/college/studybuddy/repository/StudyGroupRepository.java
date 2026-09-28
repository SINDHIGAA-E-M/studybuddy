
package com.college.studybuddy.repository;

import com.college.studybuddy.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    List<StudyGroup> findBySubjectId(Long subjectId);

    boolean existsBySubjectId(Long subjectId);
}