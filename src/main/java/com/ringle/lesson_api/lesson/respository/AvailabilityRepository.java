package com.ringle.lesson_api.lesson.respository;

import com.ringle.lesson_api.lesson.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    // 중복 체크
    boolean existsByTutorIdAndDateAndStartTime(Long tutorId, LocalDate date, LocalTime startTime);
    // 1개 찾기
    Optional<Availability> findByTutorIdAndDateAndStartTime(Long tutorId, LocalDate date, LocalTime startTime);
    // 튜터 ID와 날짜로 여러 개 찾고 싶을 때
    List<Availability> findAllByTutorIdAndDate(Long tutorId, LocalDate date);
    // 튜터 ID와 날짜 범위로 전체 찾을 수도 있음 (확장용)
    List<Availability> findAllByTutorIdAndDateBetween(Long tutorId, LocalDate startDate, LocalDate endDate);
}
