package com.ringle.lesson_api.lesson.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ReqGetTutorsDto {
    @NotNull(message = "시작 날짜를 선택해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate date;
    @NotNull(message = "시작 시간을 선택해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    LocalTime startTime;
    @NotNull(message = "수업 시간을 선택해주세요.")
    Integer durationMinutes;
}
