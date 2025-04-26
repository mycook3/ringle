package com.ringle.lesson_api.lesson.respository;

import com.ringle.lesson_api.lesson.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
