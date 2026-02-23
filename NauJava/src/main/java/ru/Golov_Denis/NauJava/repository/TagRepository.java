package ru.Golov_Denis.NauJava.repository;

import org.springframework.stereotype.Repository;
import ru.Golov_Denis.NauJava.entity.TagEntity;

import java.util.Optional;

@Repository
public interface TagRepository extends org.springframework.data.repository.CrudRepository<TagEntity, Long> {

    Optional<TagEntity> findByName(String name);
}
