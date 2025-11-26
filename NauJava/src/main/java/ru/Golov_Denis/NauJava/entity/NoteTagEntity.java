package ru.Golov_Denis.NauJava.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "note_tag")
public class NoteTagEntity {

    @EmbeddedId
    private NoteTagId id;

    @MapsId("noteId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "note_id", nullable = false)
    private NoteEntity note;

    @MapsId("tagId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;

    public NoteTagEntity() {}

    public NoteTagEntity(NoteEntity note, TagEntity tag) {
        this.note = note;
        this.tag = tag;
        this.id = new NoteTagId(note != null ? note.getId() : null, tag != null ? tag.getId() : null);
    }

    public NoteTagId getId() {
        return id;
    }

    public void setId(NoteTagId id) {
        this.id = id;
    }

    public NoteEntity getNote() {
        return note;
    }

    public void setNote(NoteEntity note) {
        this.note = note;
    }

    public TagEntity getTag() {
        return tag;
    }

    public void setTag(TagEntity tag) {
        this.tag = tag;
    }
}
