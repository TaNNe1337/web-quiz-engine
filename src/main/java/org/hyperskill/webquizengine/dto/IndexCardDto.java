package org.hyperskill.webquizengine.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

    public IndexCardDto id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public IndexCardDto title(String title) {
        this.title = title;
        return this;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public IndexCardDto text(String text) {
        this.text = text;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public IndexCardDto answer(String answer) {
        this.answer = answer;
        return this;
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
