package ru.Golov_Denis.NauJava.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NoteTagId implements Serializable {

    private Long noteId;

    private Long tagId;

    public NoteTagId() {}

    public NoteTagId(Long noteId, Long tagId) {
        this.noteId = noteId;
        this.tagId = tagId;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteTagId noteTagId = (NoteTagId) o;
        return Objects.equals(noteId, noteTagId.noteId) && Objects.equals(tagId, noteTagId.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, tagId);
    }
}
