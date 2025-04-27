package com.ringle.lesson_api.lesson.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResTutorDto {
    @NotNull(message = "튜터가 존재하지 않습니다.")
    Long tutorId;
    @NotBlank(message = "시작 날짜를 선택해주세요.")
    String name;
}
