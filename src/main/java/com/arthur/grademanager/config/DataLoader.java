package com.arthur.grademanager.config;

import com.arthur.grademanager.model.Course;
import com.arthur.grademanager.model.Grade;
import com.arthur.grademanager.model.Student;
import com.arthur.grademanager.repository.CourseRepository;
import com.arthur.grademanager.repository.GradeRepository;
import com.arthur.grademanager.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataLoader {

    @Bean
    CommandLineRunner loadData(StudentRepository studentRepo,
                               CourseRepository courseRepo,
                               GradeRepository gradeRepo) {
        return args -> {
            log.info("Loading demo data...");

            // Students
            Student s1 = studentRepo.save(Student.builder()
                    .name("Arthur Cavalcante").registration("2024001").email("arthur@ifce.edu.br").build());
            Student s2 = studentRepo.save(Student.builder()
                    .name("Maria Silva").registration("2024002").email("maria@ifce.edu.br").build());
            Student s3 = studentRepo.save(Student.builder()
                    .name("João Ferreira").registration("2024003").email("joao@ifce.edu.br").build());

            // Courses
            Course c1 = courseRepo.save(Course.builder()
                    .name("Algorithms & Data Structures").code("ADS101").workload(80).build());
            Course c2 = courseRepo.save(Course.builder()
                    .name("Object-Oriented Programming").code("OOP201").workload(60).build());
            Course c3 = courseRepo.save(Course.builder()
                    .name("Database Systems").code("DBS301").workload(60).build());
            Course c4 = courseRepo.save(Course.builder()
                    .name("Web Development").code("WEB401").workload(80).build());

            // Grades
            List<Grade> grades = List.of(
                Grade.builder().student(s1).course(c1).value(9.5).date(LocalDate.of(2024, 6, 15)).build(),
                Grade.builder().student(s1).course(c2).value(8.0).date(LocalDate.of(2024, 6, 20)).build(),
                Grade.builder().student(s1).course(c3).value(7.5).date(LocalDate.of(2024, 6, 25)).build(),
                Grade.builder().student(s2).course(c1).value(6.0).date(LocalDate.of(2024, 6, 15)).build(),
                Grade.builder().student(s2).course(c2).value(4.5).date(LocalDate.of(2024, 6, 20)).build(),
                Grade.builder().student(s2).course(c4).value(8.5).date(LocalDate.of(2024, 6, 28)).build(),
                Grade.builder().student(s3).course(c3).value(10.0).date(LocalDate.of(2024, 6, 25)).build(),
                Grade.builder().student(s3).course(c4).value(9.0).date(LocalDate.of(2024, 6, 28)).build()
            );
            gradeRepo.saveAll(grades);

            log.info("Demo data loaded: {} students, {} courses, {} grades",
                    studentRepo.count(), courseRepo.count(), gradeRepo.count());
        };
    }
}
