package com.arthur.grademanager.service;

import com.arthur.grademanager.dto.StudentDTO;
import com.arthur.grademanager.exception.BusinessException;
import com.arthur.grademanager.exception.ResourceNotFoundException;
import com.arthur.grademanager.mapper.StudentMapper;
import com.arthur.grademanager.model.Student;
import com.arthur.grademanager.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Transactional(readOnly = true)
    public List<StudentDTO.Response> findAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentDTO.Response findById(Long id) {
        return studentMapper.toResponse(getStudentOrThrow(id));
    }

    @Transactional
    public StudentDTO.Response create(StudentDTO.Request request) {
        if (studentRepository.existsByRegistration(request.registration())) {
            throw new BusinessException("Registration number already in use: " + request.registration());
        }
        if (studentRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already in use: " + request.email());
        }

        Student student = Student.builder()
                .name(request.name())
                .registration(request.registration())
                .email(request.email())
                .build();

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentDTO.Response update(Long id, StudentDTO.Request request) {
        Student student = getStudentOrThrow(id);

        boolean registrationTaken = studentRepository.findByRegistration(request.registration())
                .map(s -> !s.getId().equals(id))
                .orElse(false);
        if (registrationTaken) {
            throw new BusinessException("Registration number already in use: " + request.registration());
        }

        boolean emailTaken = studentRepository.findByEmail(request.email())
                .map(s -> !s.getId().equals(id))
                .orElse(false);
        if (emailTaken) {
            throw new BusinessException("Email already in use: " + request.email());
        }

        student.setName(request.name());
        student.setRegistration(request.registration());
        student.setEmail(request.email());

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Transactional
    public void delete(Long id) {
        getStudentOrThrow(id);
        studentRepository.deleteById(id);
    }

    public Student getStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}
