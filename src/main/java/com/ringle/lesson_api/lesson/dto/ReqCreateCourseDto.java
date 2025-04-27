package com.ringle.lesson_api.lesson.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqCreateCourseDto {
    @NotNull(message = "학생 ID를 입력해주세요.")
    Long studentId;

    @NotNull(message = "튜터 ID를 입력해주세요.")
    Long tutorId;

    @NotNull(message = "수업 날짜를 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate date;

    @NotNull(message = "시작 시간을 입력해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime startTime;

    @NotNull(message = "수업 길이를 입력해주세요.")
    Integer durationMinutes;  // 30 또는 60
}