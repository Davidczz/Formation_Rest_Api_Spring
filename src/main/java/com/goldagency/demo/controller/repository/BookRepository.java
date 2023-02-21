package com.goldagency.demo.controller.repository;

import com.goldagency.demo.controller.book.Book;
import com.goldagency.demo.controller.book.BookStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    List<Book> findByBookStatusAndUserIdNotAndDeletedFalse(BookStatus bookStatus, Integer userId);
    List<Book> findByUserIdAndDeletedFalse(Integer id);
}
