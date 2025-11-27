package ru.Golov_Denis.NauJava;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Golov_Denis.NauJava.entity.*;
import ru.Golov_Denis.NauJava.repository.*;
import ru.Golov_Denis.NauJava.service.NotesServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotesServiceImplTest {

    @Mock
    private NotesRepository notesRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private NoteTagRepository noteTagRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotesServiceImpl notesService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("user1");
        testUser.setEmail("u1@example.com");
        testUser.setPassword("encoded");
        testUser.setRole(Role.USER);
    }

    @Test
    void createNote_WithNewTag_ShouldCreateNoteAndTagAndNoteTag() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        when(notesRepository.save(any(NoteEntity.class))).thenAnswer(invocation -> {
            NoteEntity n = invocation.getArgument(0);
            n.setId(100L);
            return n;
        });

        when(tagRepository.findByName("tag1")).thenReturn(Optional.empty());
        when(tagRepository.save(any(TagEntity.class))).thenAnswer(invocation -> {
            TagEntity t = invocation.getArgument(0);
            t.setId(10L);
            return t;
        });

        notesService.createNote(null, "Title", "Content", List.of("tag1"));

        verify(userRepository, times(1)).findById(1L);
        verify(notesRepository, times(2)).save(any(NoteEntity.class));
        verify(tagRepository, times(1)).save(any(TagEntity.class));
        verify(noteTagRepository, times(1)).save(any(NoteTagEntity.class));
    }

    @Test
    void createNote_WhenUserNotFound_ShouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notesService.createNote(null, "t", "c", Collections.emptyList()));

        assertEquals("User not found", ex.getMessage());
        verify(notesRepository, never()).save(any());
    }

    @Test
    void updateNoteContent_WhenNoteExists_ShouldSaveUpdated() {
        var note = new NoteEntity();
        note.setId(5L);
        note.setContent("old");
        when(notesRepository.findById(5L)).thenReturn(Optional.of(note));
        when(notesRepository.save(any(NoteEntity.class))).thenAnswer(i -> i.getArgument(0));

        notesService.updateNoteContent(5L, "new");

        assertEquals("new", note.getContent());
        verify(notesRepository, times(1)).save(note);
    }

    @Test
    void updateNoteContent_WhenNoteNotExists_ShouldDoNothing() {
        when(notesRepository.findById(999L)).thenReturn(Optional.empty());
        notesService.updateNoteContent(999L, "new");
        verify(notesRepository, never()).save(any());
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        notesService.deleteById(42L);
        verify(notesRepository, times(1)).deleteById(42L);
    }

    @Test
    void findById_WhenNotFound_ShouldReturnNull() {
        when(notesRepository.findById(123L)).thenReturn(Optional.empty());
        var res = notesService.findById(123L);
        assertNull(res);
    }
}
