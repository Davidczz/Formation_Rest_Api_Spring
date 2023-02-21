package com.goldagency.demo.controller.book;

import com.goldagency.demo.controller.category.Category;
import com.goldagency.demo.controller.user.User;
import jakarta.persistence.*;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank
    private String title;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    private BookStatus bookStatus;

    @Transient
    private int categoryId;

    private boolean deleted;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
