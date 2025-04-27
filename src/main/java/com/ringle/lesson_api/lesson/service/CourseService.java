package com.ringle.lesson_api.lesson.service;

import com.ringle.lesson_api.lesson.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CourseService {
    /**
     * @param : 날짜, 수업 시간
     *          date : 원하는 날짜
     *          durationMinutes : 수업 시간
     *
     * @return 수업 가능한 시간대
     */
    public List<LocalTime> getAvailableTimes(ReqGetAvailableTimeDto reqGetAvailableTimeDto);
    List<ResTutorDto> getAvailableTutors(ReqGetTutorsDto reqGetTutorsDto);
}
