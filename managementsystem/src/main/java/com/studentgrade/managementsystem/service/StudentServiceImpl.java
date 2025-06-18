package com.studentgrade.managementsystem.service;

import com.studentgrade.managementsystem.entity.Student;
import com.studentgrade.managementsystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student addStudent(Student student) {
        calculateAndSetGrades(student);
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, Student updatedStudent) {
        Student existing = studentRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(updatedStudent.getName());
            existing.setMathMarks(updatedStudent.getMathMarks());
            existing.setScienceMarks(updatedStudent.getScienceMarks());
            existing.setEnglishMarks(updatedStudent.getEnglishMarks());
            calculateAndSetGrades(existing);
            return studentRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    private void calculateAndSetGrades(Student student) {
        double avg = (student.getMathMarks() + student.getScienceMarks() + student.getEnglishMarks()) / 3.0;
        student.setAverageMarks(avg);
        student.setGrade(assignGrade(avg));
    }

    private String assignGrade(double avg) {
        if (avg >= 90) return "A";
        else if (avg >= 80) return "B";
        else if (avg >= 70) return "C";
        else if (avg >= 60) return "D";
        else return "F";
    }
}
