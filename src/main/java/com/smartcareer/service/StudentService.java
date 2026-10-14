package com.smartcareer.service;

import com.smartcareer.entity.Student;
import com.smartcareer.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(Student student) {

        if (student.getUser() == null) {
            throw new RuntimeException("Student must be linked to a user");
        }

        if (studentRepository.existsByUserId(student.getUser().getId())) {
            throw new RuntimeException("Student profile already exists for this user");
        }

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {

        Optional<Student> student = studentRepository.findById(id);

        if (student.isEmpty()) {
            throw new RuntimeException("Student not found with ID: " + id);
        }

        return student.get();
    }

    // public Student getStudentByUserId(Long userId) {

    //     Optional<Student> student = studentRepository.findByUserId(userId);

    //     if (student.isEmpty()) {
    //         throw new RuntimeException("Student profile not found for user ID: " + userId);
    //     }

    //     return student.get();
    // }
public Student getStudentByUserId(Long userId) {
    return studentRepository.findByUserId(userId).orElse(null);
}
    public Student updateStudent(Long id, Student updatedStudent) {

        Student existingStudent = getStudentById(id);

        existingStudent.setPhone(updatedStudent.getPhone());
        existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
        existingStudent.setGender(updatedStudent.getGender());
        existingStudent.setCollegeName(updatedStudent.getCollegeName());
        existingStudent.setCourse(updatedStudent.getCourse());
        existingStudent.setGraduationYear(updatedStudent.getGraduationYear());
        existingStudent.setCgpa(updatedStudent.getCgpa());
        existingStudent.setPercentage(updatedStudent.getPercentage());
        existingStudent.setBacklogs(updatedStudent.getBacklogs());

        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {

        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with ID: " + id);
        }

        studentRepository.deleteById(id);
    }
}
