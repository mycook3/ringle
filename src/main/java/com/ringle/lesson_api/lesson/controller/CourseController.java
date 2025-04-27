package com.ringle.lesson_api.lesson.controller;

import com.ringle.lesson_api.lesson.dto.*;
import com.ringle.lesson_api.lesson.service.AvailabilityService;
import com.ringle.lesson_api.lesson.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/")
    public String hello() {
        return "Hello, Student!";
    }

    @GetMapping("/available-times")
    public List<LocalTime> getAvailableTimes(@Valid @ModelAttribute ReqGetAvailableTimeDto request) {
        return courseService.getAvailableTimes(request);
    }

    @GetMapping("/available-tutors")
    public List<ResTutorDto> getAvailableTutors(@Valid @ModelAttribute ReqGetTutorsDto request) {
        return courseService.getAvailableTutors(request);
    }

    @PostMapping
    public ResponseEntity<Void> createCourse(@Valid @RequestBody ReqCreateCourseDto request) {
        courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
