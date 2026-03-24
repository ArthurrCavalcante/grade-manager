package com.arthur.grademanager.service;

import com.arthur.grademanager.dto.CourseDTO;
import com.arthur.grademanager.exception.BusinessException;
import com.arthur.grademanager.exception.ResourceNotFoundException;
import com.arthur.grademanager.mapper.CourseMapper;
import com.arthur.grademanager.model.Course;
import com.arthur.grademanager.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional(readOnly = true)
    public List<CourseDTO.Response> findAll() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseDTO.Response findById(Long id) {
        return courseMapper.toResponse(getCourseOrThrow(id));
    }

    @Transactional
    public CourseDTO.Response create(CourseDTO.Request request) {
        if (courseRepository.existsByCode(request.code())) {
            throw new BusinessException("Course code already exists: " + request.code());
        }
        Course course = Course.builder()
                .name(request.name())
                .code(request.code())
                .workload(request.workload())
                .build();
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseDTO.Response update(Long id, CourseDTO.Request request) {
        Course course = getCourseOrThrow(id);

        boolean codeTaken = courseRepository.findByCode(request.code())
                .map(c -> !c.getId().equals(id))
                .orElse(false);
        if (codeTaken) {
            throw new BusinessException("Course code already in use: " + request.code());
        }

        course.setName(request.name());
        course.setCode(request.code());
        course.setWorkload(request.workload());
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Transactional
    public void delete(Long id) {
        getCourseOrThrow(id);
        courseRepository.deleteById(id);
    }

    public Course getCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }
}
