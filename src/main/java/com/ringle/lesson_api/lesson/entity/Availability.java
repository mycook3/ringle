package com.ringle.lesson_api.lesson.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "availability",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"tutor_id", "startTime"})
        }
)
public class Availability {
    /**
     * id : pk
     * tutor : 강사
     * date : 날짜
     * startTime : 시작하는 시간
     * durationMinutes : 수업 시간
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    private User tutor;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Integer durationMinutes;

    public Availability(User tutor, LocalDate Date, LocalTime startTime) {
        this.tutor = tutor;
        this.date = Date;
        this.startTime = startTime;
    }
}