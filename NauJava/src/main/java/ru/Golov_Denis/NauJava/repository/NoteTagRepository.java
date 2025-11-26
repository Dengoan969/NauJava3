package ru.Golov_Denis.NauJava.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import ru.Golov_Denis.NauJava.entity.NoteTagEntity;
import ru.Golov_Denis.NauJava.entity.NoteTagId;

import java.util.List;

@RepositoryRestResource(path = "note-tags")
@Repository
public interface NoteTagRepository extends org.springframework.data.repository.CrudRepository<NoteTagEntity, NoteTagId> {

    List<NoteTagEntity> findByNote_Id(Long noteId);

    List<NoteTagEntity> findByTag_Id(Long tagId);
}
