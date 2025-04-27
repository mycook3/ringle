package com.ringle.lesson_api.lesson.service;

import com.ringle.lesson_api.lesson.dto.*;
import com.ringle.lesson_api.lesson.entity.Availability;
import com.ringle.lesson_api.lesson.entity.User;
import com.ringle.lesson_api.lesson.respository.AvailabilityRepository;
import com.ringle.lesson_api.lesson.respository.CourseRepository;
import com.ringle.lesson_api.lesson.respository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
        Integer durationMinutes = reqGetTutorsDto.getDurationMinutes();
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
}
