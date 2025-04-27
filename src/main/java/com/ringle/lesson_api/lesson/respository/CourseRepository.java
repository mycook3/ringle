package com.ringle.lesson_api.lesson.respository;

import com.ringle.lesson_api.lesson.entity.Availability;
import com.ringle.lesson_api.lesson.entity.Course;
import com.ringle.lesson_api.lesson.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // 날짜를 기준으로 예약된 강의 불러오기
    @Query("select c.availability from Course c where c.availability.date = :date")
    List<Availability> findAllReservedAvailabilities(@Param("date") LocalDate date);
}
