package ru.Golov_Denis.NauJava;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import ru.Golov_Denis.NauJava.controller.ExceptionControllerAdvice;
import ru.Golov_Denis.NauJava.controller.NotesController;
import ru.Golov_Denis.NauJava.entity.*;
import ru.Golov_Denis.NauJava.repository.NotesRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

class NotesControllerRestAssuredTest {

    @Mock
    private NotesRepository notesRepository;

    private NotesController notesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notesController = new NotesController(notesRepository);
        RestAssuredMockMvc.standaloneSetup(notesController, new ExceptionControllerAdvice());
    }

    @Test
    void getNote_WhenExists_ShouldReturnDto() {
        var user = new UserEntity();
        user.setUsername("user1");

        var note = new NoteEntity();
        note.setId(1L);
        note.setTitle("T1");
        note.setContent("C1");
        note.setUser(user);

        when(notesRepository.findById(1L)).thenReturn(Optional.of(note));

        RestAssuredMockMvc.given()
                .when()
                .get("/api/notes/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("T1"))
                .body("content", equalTo("C1"))
                .body("username", equalTo("user1"));
    }

    @Test
    void getNote_WhenNotFound_ShouldReturn404() {
        when(notesRepository.findById(999L)).thenReturn(Optional.empty());

        RestAssuredMockMvc.given()
                .when()
                .get("/api/notes/999")
                .then()
                .statusCode(404)
                .body("message", containsString("Note not found"));
    }

    @Test
    void getNotesByUsername_ShouldReturnList() {
        var user = new UserEntity();
        user.setUsername("user1");

        var n1 = new NoteEntity();
        n1.setId(2L);
        n1.setTitle("A");
        n1.setUser(user);

        var list = List.of(n1);
        when(notesRepository.findByUserUsernameCriteria("user1")).thenReturn(list);

        RestAssuredMockMvc.given()
                .when()
                .get("/api/notes/by-user/user1")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].username", equalTo("user1"))
                .body("[0].id", equalTo(2));
    }

    @Test
    void searchNotes_ShouldReturnResults() {
        var user = new UserEntity();
        user.setUsername("u");

        var n1 = new NoteEntity();
        n1.setId(3L);
        n1.setTitle("Some title");
        n1.setContent("contains frag");
        n1.setUser(user);

        when(notesRepository.findByTitleOrContentFragmentCriteria("frag")).thenReturn(List.of(n1));

        RestAssuredMockMvc.given()
                .queryParam("fragment", "frag")
                .when()
                .get("/api/notes/search")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].id", equalTo(3));
    }
}
