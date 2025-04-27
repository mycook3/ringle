package com.ringle.lesson_api.lesson.controller;

import com.ringle.lesson_api.lesson.dto.ReqCreateAvailabilityDto;
import com.ringle.lesson_api.lesson.dto.ReqDeleteAvailabilityDto;
import com.ringle.lesson_api.lesson.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {
    private final AvailabilityService availabilityService;
    @GetMapping("/")
    public String hello() {
        return "Hello, tutor!";
    }
    @PostMapping
    public ResponseEntity<Void> createAvailability(@RequestBody ReqCreateAvailabilityDto request) {
        availabilityService.createAvailability(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAvailability(@RequestBody ReqDeleteAvailabilityDto request) {
        availabilityService.deleteAvailability(request);
        return ResponseEntity.noContent().build();
    }
}
