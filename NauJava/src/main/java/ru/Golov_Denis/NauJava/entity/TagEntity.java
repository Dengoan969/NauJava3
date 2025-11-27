package ru.Golov_Denis.NauJava.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tags")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "tag")
    private List<NoteTagEntity> noteTags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NoteTagEntity> getNoteTags() {
        return noteTags;
    }

    public void setNoteTags(List<NoteTagEntity> noteTags) {
        this.noteTags = noteTags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
