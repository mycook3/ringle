package com.ringle.lesson_api.lesson.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    /**
     * id : pk
     * username : 사용자 이름
     * email : 사용자 email id
     * role : 강사 or 학생 (향후 관리자 추가 필요할 수 있음)
     */

    public enum Role {
        STUDENT, TUTOR, ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
}
