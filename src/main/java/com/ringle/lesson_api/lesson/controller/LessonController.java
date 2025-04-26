package com.ringle.lesson_api.lesson.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LessonController {
    @GetMapping("/")
    public String hello() {
        return "Hello, Spring!";
    }
}
