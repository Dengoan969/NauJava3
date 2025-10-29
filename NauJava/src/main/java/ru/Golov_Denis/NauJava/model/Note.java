package ru.Golov_Denis.NauJava.model;

import java.util.List;

public class Note {
    private Long id;

    private String title;

    private String content;

    private List<String> tags;

    public Note(Long id, String title, String content, List<String> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


    @Override
    public String toString() {
        return "Заметка " + id + ". " + title + "\n" +
                content + "\n" +
                "Теги: " + String.join(", ", tags);
    }
}
