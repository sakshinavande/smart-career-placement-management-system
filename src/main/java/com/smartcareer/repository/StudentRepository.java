
package com.smartcareer.repository;

import com.smartcareer.entity.Student;
import com.smartcareer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    // Find the student linked to the logged-in user
    Optional<Student> findByUser(User user);
}
