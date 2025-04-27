package com.ringle.lesson_api.lesson.service;

import com.ringle.lesson_api.lesson.dto.*;
import com.ringle.lesson_api.lesson.entity.Availability;
import com.ringle.lesson_api.lesson.entity.Course;
import com.ringle.lesson_api.lesson.entity.User;
import com.ringle.lesson_api.lesson.respository.AvailabilityRepository;
import com.ringle.lesson_api.lesson.respository.CourseRepository;
import com.ringle.lesson_api.lesson.respository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final AvailabilityRepository availabilityRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public List<LocalTime> getAvailableTimes(ReqGetAvailableTimeDto reqGetAvailableTimeDto) {
        LocalDate date = reqGetAvailableTimeDto.getDate();
        List<Availability> allAvailabilities = availabilityRepository.findAllByDate(date);

        // 이미 예약된 강의 제외
        List<Availability> reservedAvailabilities = courseRepository.findAllReservedAvailabilities(date);
        Set<Long> reservedAvailabilityIds = reservedAvailabilities.stream()
                .map(Availability::getId)
                .collect(Collectors.toSet());

        List<Availability> available = allAvailabilities.stream()
                .filter(a -> !reservedAvailabilityIds.contains(a.getId()))
                .collect(Collectors.toList());

        List<LocalTime> times = available.stream()
                .map(Availability::getStartTime)
                .sorted()
                .collect(Collectors.toList());

        List<LocalTime> result = new ArrayList<>();

        Integer durationMinutes = reqGetAvailableTimeDto.getDurationMinutes();
        if (durationMinutes == 30) {
            result.addAll(times);
        } else if (durationMinutes == 60) {
            for (int i = 0; i < times.size() - 1; i++) {
                LocalTime now = times.get(i);
                LocalTime next = times.get(i + 1);

                if (now.plusMinutes(30).equals(next)) {
                    result.add(now); // 60분 수업 가능한 시작 시간
                }
            }
        } else {
            throw new IllegalArgumentException("수업 길이는 30분 또는 60분이어야 합니다.");
        }

        return result;
    }

    public List<ResTutorDto> getAvailableTutors(ReqGetTutorsDto reqGetTutorsDto) {
        LocalDate date = reqGetTutorsDto.getDate();
        LocalTime startTime = reqGetTutorsDto.getStartTime();
        int durationMinutes = reqGetTutorsDto.getDurationMinutes();
        // 기본적으로 30분짜리 슬롯 찾기
        List<Availability> firstSlots = availabilityRepository
                .findAllByDateAndStartTime(date, startTime);

        if (durationMinutes == 30) {
            // 30분 수업이면, 해당 slot만 예약 안 된 튜터 리턴
            return firstSlots.stream()
                    .filter(a -> !courseRepository.existsByAvailability(a))
                    .map(a -> new ResTutorDto(a.getTutor().getId(), a.getTutor().getUsername()))
                    .toList();
        }
        else if (durationMinutes == 60) {
            // 60분 수업이면, 연속된 2개 슬롯을 가진 튜터만 리턴

            LocalTime nextStartTime = startTime.plusMinutes(30);

            List<Availability> secondSlots = availabilityRepository
                    .findAllByDateAndStartTime(date, nextStartTime);

            // 튜터 ID 기준으로 묶기
            Map<Long, Availability> secondSlotMap = secondSlots.stream()
                    .collect(Collectors.toMap(
                            a -> a.getTutor().getId(),
                            a -> a
                    ));

            return firstSlots.stream()
                    .filter(first -> {
                        // 첫 번째 슬롯 예약 안 되어야 하고
                        boolean firstNotReserved = !courseRepository.existsByAvailability(first);

                        // 같은 튜터가 바로 다음 슬롯도 열어놔야 하고
                        Availability second = secondSlotMap.get(first.getTutor().getId());

                        // 두 번째 슬롯도 예약 안 되어야 함
                        boolean secondAvailable = (second != null) && (!courseRepository.existsByAvailability(second));

                        return firstNotReserved && secondAvailable;
                    })
                    .map(a -> new ResTutorDto(a.getTutor().getId(), a.getTutor().getUsername()))
                    .toList();
        }
        else {
            throw new IllegalArgumentException("수업 길이는 30분 또는 60분이어야 합니다.");
        }
    }

    @Transactional
    public void createCourse(ReqCreateCourseDto reqCreateCourseDto) {
        Long studentId = reqCreateCourseDto.getStudentId();
        Long tutorId = reqCreateCourseDto.getTutorId();
        LocalDate date = reqCreateCourseDto.getDate();
        LocalTime startTime = reqCreateCourseDto.getStartTime();
        int durationMinutes = reqCreateCourseDto.getDurationMinutes();

        // Student / Tutor 검증
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("학생을 찾을 수 없습니다."));
        if (student.getRole() != User.Role.STUDENT) {
            throw new IllegalArgumentException("해당 사용자는 학생이 아닙니다.");
        }

        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new EntityNotFoundException("튜터를 찾을 수 없습니다."));
        if (tutor.getRole() != User.Role.TUTOR) {
            throw new IllegalArgumentException("해당 사용자는 튜터가 아닙니다.");
        }

        // 첫 번째 30분 슬롯 찾기
        Availability firstSlot = availabilityRepository
                .findByTutorIdAndDateAndStartTime(tutorId, date, startTime)
                .orElseThrow(() -> new EntityNotFoundException("해당 시간에 튜터가 오픈하지 않았습니다."));

        // 예약 중복 체크
        if (courseRepository.existsByAvailability(firstSlot)) {
            throw new IllegalStateException("이미 예약된 시간입니다.");
        }

        // duration 에 따라 저장
        if (durationMinutes == 30) {
            // 30분 수업: 첫 슬롯만 저장
            Course c = Course.builder()
                    .student(student)
                    .availability(firstSlot)
                    .build();
            courseRepository.save(c);

        } else if (durationMinutes == 60) {
            // 60분 수업: 두 번째 슬롯도 체크하고, 두 개 모두 저장
            LocalTime nextStart = startTime.plusMinutes(30);
            Availability secondSlot = availabilityRepository
                    .findByTutorIdAndDateAndStartTime(tutorId, date, nextStart)
                    .orElseThrow(() -> new EntityNotFoundException("60분 수업을 위한 다음 30분 슬롯이 없습니다."));

            if (courseRepository.existsByAvailability(secondSlot)) {
                throw new IllegalStateException("이미 예약된 시간 (다음 30분 슬롯 포함) 이 있습니다.");
            }

            // 두 개의 Course 레코드 생성
            Course c1 = Course.builder()
                    .student(student)
                    .availability(firstSlot)
                    .build();
            Course c2 = Course.builder()
                    .student(student)
                    .availability(secondSlot)
                    .build();

            courseRepository.saveAll(List.of(c1, c2));

        } else {
            throw new IllegalArgumentException("수업 길이는 30분 또는 60분만 가능합니다.");
        }
    }

    public List<ResCourseDto> getMyCourses(Long studentId) {
        List<Course> courses = courseRepository.findAllByStudentIdOrderByAvailabilityDateAscAvailabilityStartTimeAsc(studentId);

        return courses.stream()
                .map(course -> new ResCourseDto(
                        course.getAvailability().getDate(),
                        course.getAvailability().getStartTime(),
                        course.getAvailability().getTutor().getUsername()
                ))
                .toList();
    }
}
