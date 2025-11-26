package ru.Golov_Denis.NauJava.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.Golov_Denis.NauJava.entity.UserEntity;

import java.util.Optional;

@RepositoryRestResource(path = "users")
@Repository
public interface UserRepository extends org.springframework.data.repository.CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);
}
