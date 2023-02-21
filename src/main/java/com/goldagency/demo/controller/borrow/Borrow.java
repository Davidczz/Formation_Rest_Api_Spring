package com.goldagency.demo.controller.borrow;

import com.goldagency.demo.controller.book.Book;
import com.goldagency.demo.controller.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Borrow {
    @ManyToOne
    private User borrower;
    @ManyToOne
    private User lender;

    @ManyToOne
    private Book book;

    private LocalDate askDate;

    private LocalDate closeDate;
    @Id
    @GeneratedValue
    private Long id;


    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public User getLender() {
        return lender;
    }

    public void setLender(User lender) {
        this.lender = lender;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getAskDate() {
        return askDate;
    }

    public void setAskDate(LocalDate askDate) {
        this.askDate = askDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
