package ru.Golov_Denis.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.Golov_Denis.NauJava.entity.NoteEntity;
import ru.Golov_Denis.NauJava.entity.TagEntity;
import ru.Golov_Denis.NauJava.entity.NoteTagEntity;
import ru.Golov_Denis.NauJava.repository.NotesRepository;
import ru.Golov_Denis.NauJava.repository.TagRepository;
import ru.Golov_Denis.NauJava.repository.NoteTagRepository;
import ru.Golov_Denis.NauJava.repository.UserRepository;
import ru.Golov_Denis.NauJava.service.NoteTransactionalService;

import java.util.UUID;

@SpringBootTest(properties = "spring.profiles.active=test")
public class NotesRepositoryTests {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private NoteTagRepository noteTagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteTransactionalService noteTransactionalService;

    @Test
    void testFindByTitleContainingIgnoreCase() {
        var uniqueTitle = "TestTitle_" + UUID.randomUUID();
        var note = new NoteEntity();
        note.setTitle(uniqueTitle);
        note.setContent("Some content");
        note.setUser(userRepository.findById(1L).orElseThrow());
        notesRepository.save(note);

        var found = notesRepository.findByTitleContainingIgnoreCase("testtitle");
        Assertions.assertFalse(found.isEmpty());
        Assertions.assertTrue(found.stream().anyMatch(n -> n.getId().equals(note.getId())));
    }

    @Test
    void testFindByUserUsernameCriteria() {
        var username = "user1";
        var notes = notesRepository.findByUserUsernameCriteria(username);
        Assertions.assertNotNull(notes);
    }

    @Test
    void testFindByTitleOrContentFragmentCriteria() {
        var fragment = "uniquecontent_" + UUID.randomUUID();
        var note = new NoteEntity();
        note.setTitle("Title " + fragment);
        note.setContent("Content " + fragment);
        note.setUser(userRepository.findById(1L).orElseThrow());
        notesRepository.save(note);

        var found = notesRepository.findByTitleOrContentFragmentCriteria(fragment);
        Assertions.assertFalse(found.isEmpty());
        Assertions.assertTrue(found.stream().anyMatch(n -> n.getId().equals(note.getId())));
    }

    @Test
    void testDeleteNoteWithTags() {
        var note = new NoteEntity();
        note.setTitle("TestNote");
        note.setContent("Content");
        note.setUser(userRepository.findById(1L).orElseThrow());
        notesRepository.save(note);

        var tag = new TagEntity();
        tag.setName("TestTag_" + UUID.randomUUID());
        tagRepository.save(tag);

        var nt = new NoteTagEntity(note, tag);
        noteTagRepository.save(nt);

        var ntBefore = noteTagRepository.findByNote_Id(note.getId());
        Assertions.assertFalse(ntBefore.isEmpty());

        noteTransactionalService.deleteNoteWithTags(note.getId());

        Assertions.assertTrue(notesRepository.findById(note.getId()).isEmpty());
        var ntAfter = noteTagRepository.findByNote_Id(note.getId());
        Assertions.assertTrue(ntAfter.isEmpty());
        Assertions.assertTrue(tagRepository.findById(tag.getId()).isPresent());
    }

    @Test
    void testDeleteNoteWithTags_NotExist() {
        var fakeId = 999_999L;
        Assertions.assertThrows(RuntimeException.class,
                () -> noteTransactionalService.deleteNoteWithTags(fakeId));
    }
}
