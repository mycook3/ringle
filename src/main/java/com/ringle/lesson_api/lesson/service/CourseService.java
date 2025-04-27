package com.ringle.lesson_api.lesson.service;

import com.ringle.lesson_api.lesson.dto.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CourseService {
    /**
     * @param  reqGetAvailableTimeDto :
     *                                <br>date : 원하는 날짜
     *                                <br>durationMinutes : 수업 시간
     * @return 수업 가능한 시간 목록
     */
    List<LocalTime> getAvailableTimes(ReqGetAvailableTimeDto reqGetAvailableTimeDto);
    /**
     * @param reqGetTutorsDto :
     *                        <br>date : 원하는 날짜
     *                        <br>startTime : 수업 시작 시간
     *                        <br>durationMinutes : 수업 시간
     * @return 수업 신청 가능한 튜터 목록
     */
    List<ResTutorDto> getAvailableTutors(ReqGetTutorsDto reqGetTutorsDto);
    /**
     * @param reqCreateCourseDto :
     *                           <br>studentId : 신청하는 학생 ID
     *                           <br>tutorId : 튜터 ID
     *                           <br>date : 수업 날짜
     *                           <br>startTime : 수업 시작 시간
     *                           <br>durationMinutes : 수업 시간
     */
    void createCourse(ReqCreateCourseDto reqCreateCourseDto);
    /**
     * @param studentId : 예약한 강의를 조회할 학생 ID
     * @return 예약한 강의 목록
     */
    public List<ResCourseDto> getMyCourses(Long studentId);

}
