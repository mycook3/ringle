package com.ringle.lesson_api.lesson.service;

import com.ringle.lesson_api.lesson.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CourseService {
    /**
     * @param  reqGetAvailableTimeDto : 원하는 날짜, 수업 시간
     * @return 수업 가능한 시간 목록
     */
    List<LocalTime> getAvailableTimes(ReqGetAvailableTimeDto reqGetAvailableTimeDto);
    /**
     * @param reqGetTutorsDto : 원하는 날짜, 시작 시간, 수업 시간
     * @return 수업 신청 가능한 튜터 목록
     */
    List<ResTutorDto> getAvailableTutors(ReqGetTutorsDto reqGetTutorsDto);
    /**
     * @param reqCreateCourseDto : 신청하는 학생 ID, 튜터 ID, 수업 날짜, 시작 시간, 수업 시간
     */
    void createCourse(ReqCreateCourseDto reqCreateCourseDto);
}
