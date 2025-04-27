package com.ringle.lesson_api.lesson.service;

import com.ringle.lesson_api.lesson.dto.ReqCreateAvailabilityDto;
import com.ringle.lesson_api.lesson.dto.ReqDeleteAvailabilityDto;

public interface AvailabilityService {
    /**
     * @param : 튜터 id, 날짜, 시작 시간
     *          toutorId : 생성하는 튜터 id
     *          localDate : 생성하는 강의의 날짜
     *          startTime : 생성하는 강의의 시작 시간
     *
     * @return 수업 생성 성공 메세지
     */
    String createAvailability(ReqCreateAvailabilityDto reqCreateAvailabilityDto);
    /**
     * @param : 튜터 id, 날짜, 시작 시간
     *          toutorId : 생성하는 튜터 id
     *          localDate : 생성하는 강의의 날짜
     *          startTime : 생성하는 강의의 시작 시간
     *
     * @return 수업 삭제 성공 메세지
     */
    String deleteAvailability(ReqDeleteAvailabilityDto reqDeleteAvailabilityDto);
    //String updateAvailability();
}
