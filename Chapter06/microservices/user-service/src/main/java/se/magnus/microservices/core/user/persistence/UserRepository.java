package se.magnus.microservices.core.user.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, String>, CrudRepository<UserEntity, String> {
    Optional<UserEntity> findByUserId(int userId);
    Optional<UserEntity> findByName(String name);
}