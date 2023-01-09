package ua.task.test.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.task.test.users.entity.UserEntity;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("SELECT u FROM UserEntity u WHERE u.email =:email")
    UserEntity findByEmail(String email);

    Page<UserEntity> findAll(Pageable pageable);

    boolean existsByEmailAndPasswordHash(String email, String passwordHash);
}
