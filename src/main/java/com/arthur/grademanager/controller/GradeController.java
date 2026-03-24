package com.arthur.grademanager.controller;

import com.arthur.grademanager.dto.GradeDTO;
import com.arthur.grademanager.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @GetMapping
    public ResponseEntity<List<GradeDTO.Response>> findAll() {
        return ResponseEntity.ok(gradeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeDTO.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(gradeService.findById(id));
    }

    @GetMapping("/report/student/{studentId}")
    public ResponseEntity<GradeDTO.StudentReport> getStudentReport(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getStudentReport(studentId));
    }

    @PostMapping
    public ResponseEntity<GradeDTO.Response> create(@RequestBody @Valid GradeDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gradeService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeDTO.Response> update(
            @PathVariable Long id,
            @RequestBody @Valid GradeDTO.Request request) {
        return ResponseEntity.ok(gradeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gradeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
