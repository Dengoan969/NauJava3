package ru.Golov_Denis.NauJava.repository.custom;

import ru.Golov_Denis.NauJava.entity.NoteEntity;
import java.util.List;

public interface NotesRepositoryCustom {

    List<NoteEntity> findByUserUsernameCriteria(String username);

    List<NoteEntity> findByTitleOrContentFragmentCriteria(String fragment);
}
