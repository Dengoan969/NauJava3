package ru.Golov_Denis.NauJava.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "notes")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "note", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<NoteTagEntity> noteTags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Заметка ").append(id).append(". ").append(title == null ? "" : title).append("\n");
        sb.append(content == null ? "" : content).append("\n");
        sb.append("Теги: ");
        if (noteTags != null && !noteTags.isEmpty()) {
            var names = new java.util.ArrayList<String>();
            for (var nt : noteTags) {
                if (nt != null && nt.getTag() != null && nt.getTag().getName() != null) {
                    names.add(nt.getTag().getName());
                }
            }
            sb.append(String.join(", ", names));
        }
        return sb.toString();
    }
}
