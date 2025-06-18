package com.studentgrade.managementsystem.repository;


import com.studentgrade.managementsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}