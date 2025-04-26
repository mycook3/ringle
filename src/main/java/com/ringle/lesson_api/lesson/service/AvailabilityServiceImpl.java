package com.ringle.lesson_api.lesson.service;

import com.ringle.lesson_api.lesson.dto.ReqCreateAvailabilityDto;
import com.ringle.lesson_api.lesson.dto.ReqDeleteAvailabilityDto;
import com.ringle.lesson_api.lesson.entity.Availability;
import com.ringle.lesson_api.lesson.entity.User;
import com.ringle.lesson_api.lesson.respository.AvailabilityRepository;
import com.ringle.lesson_api.lesson.respository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvailabilityServiceImpl implements AvailabilityService {
    private final AvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;

    @Override
    public String createAvailability(ReqCreateAvailabilityDto reqCreateAvailabilityDto){
        Long tutorId = reqCreateAvailabilityDto.getTutorId();
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("Tutor not found"));

        if (tutor.getRole() != User.Role.TUTOR) {
            throw new IllegalArgumentException("해당 사용자는 튜터가 아닙니다.");
        }

        LocalDate date = reqCreateAvailabilityDto.getDate();
        LocalTime startTime = reqCreateAvailabilityDto.getStartTime();
        boolean exists = availabilityRepository.existsByTutorIdAndDateAndStartTime(tutorId, date, startTime);
        if (exists) {
            throw new IllegalStateException("이미 등록된 수업 가능한 시간입니다.");
        }

        Availability availability = Availability.builder()
                .tutor(tutor)
                .date(date)
                .startTime(startTime)
                .durationMinutes(30) // 항상 30분 고정
                .build();

        availabilityRepository.save(availability);
        return "생성 완료";
    }
    @Override
    public String deleteAvailability(ReqDeleteAvailabilityDto reqDeleteAvailabilityDto){
        Long tutorId = reqDeleteAvailabilityDto.getTutorId();
        LocalDate Date = reqDeleteAvailabilityDto.getDate();
        LocalTime startTime = reqDeleteAvailabilityDto.getStartTime();

        Availability availability = availabilityRepository
                .findByTutorIdAndDateAndStartTime(tutorId, Date, startTime)
                .orElseThrow(() -> new EntityNotFoundException("해당 강의는 존재하지 않습니다."));

//        if (courseRepository.existsByAvailability(availability)) {
//            throw new IllegalStateException("이미 예약된 시간은 삭제할 수 없습니다.");
//        }

        availabilityRepository.delete(availability);
        return "삭제 완료";
    }
//    @Override
//    public String updateAvailability(){
//        return "수정 완료";
//    }
}
