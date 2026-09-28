

package com.college.studybuddy.repository;

import com.college.studybuddy.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}