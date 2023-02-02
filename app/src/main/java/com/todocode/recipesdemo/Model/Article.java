package com.todocode.recipesdemo.Model;

public class Article {
    public String id, title, textSmall, text, image, date;

    public Article(String id, String title, String textSmall, String text, String image, String date) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.textSmall = textSmall;
        this.image = image;
        this.date = date;
    }

    public String getTextSmall() {
        return textSmall;
    }

    public void setTextSmall(String textSmall) {
        this.textSmall = textSmall;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
