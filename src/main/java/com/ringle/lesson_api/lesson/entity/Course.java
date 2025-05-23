package com.ringle.lesson_api.lesson.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(
        name = "course",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"availability_id"})
        }
)
public class Course {
    /**
     * id : pk
     * student : 예약한 학생
     * availability : 예약한 강의
     * createdAt : 예약한 날짜
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availability_id", nullable = false, unique = true)
    private Availability availability;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}