
package com.college.studybuddy.repository;

import com.college.studybuddy.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}