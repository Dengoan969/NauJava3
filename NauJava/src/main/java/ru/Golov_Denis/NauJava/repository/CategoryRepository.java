package ru.Golov_Denis.NauJava.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.Golov_Denis.NauJava.entity.CategoryEntity;

import java.util.Optional;

@RepositoryRestResource(path = "categories")
@Repository
public interface CategoryRepository extends org.springframework.data.repository.CrudRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(String name);
}
