package com.ringle.lesson_api.lesson.service;

import com.ringle.lesson_api.lesson.dto.ReqCreateAvailabilityDto;
import com.ringle.lesson_api.lesson.dto.ReqDeleteAvailabilityDto;
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
    public List<LocalTime> getAvailableTimes(LocalDate date, int durationMinutes){
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
}
