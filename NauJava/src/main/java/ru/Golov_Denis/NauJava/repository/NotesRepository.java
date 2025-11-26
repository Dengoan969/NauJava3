package ru.Golov_Denis.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.Golov_Denis.NauJava.entity.NoteEntity;
import ru.Golov_Denis.NauJava.repository.custom.NotesRepositoryCustom;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotesRepository extends CrudRepository<NoteEntity, Long>, NotesRepositoryCustom {

    List<NoteEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("select n from NoteEntity n where n.user.username = :username")
    List<NoteEntity> findByUserUsername(@Param("username") String username);

    List<NoteEntity> findByTitleContainingIgnoreCase(String fragment);

    List<NoteEntity> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
            String titleFragment,
            String contentFragment
    );

    @Override
    Optional<NoteEntity> findById(Long id);
}
