package org.hyperskill.webquizengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IndexCardDto {

    private Long id;

    @NotNull(message = "Index card must have a title")
    @NotEmpty(message = "Index card title should not be empty")
    private String title;

    @NotNull(message = "Index card must have a text")
    @NotEmpty(message = "Index card text should not be empty")
    private String text;

    @NotNull(message = "Index card must have an answer")
    @NotEmpty(message = "Index card answer should not be empty")
    private String answer;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", answer=" + answer +
                '}';
    }
}
