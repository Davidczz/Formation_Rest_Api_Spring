package com.goldagency.demo.controller.book;

import com.goldagency.demo.controller.borrow.Borrow;
import com.goldagency.demo.controller.category.Category;
import com.goldagency.demo.controller.repository.BookRepository;
import com.goldagency.demo.controller.repository.BorrowRepository;
import com.goldagency.demo.controller.repository.CategoryRepository;
import com.goldagency.demo.controller.repository.UserRepository;
import com.goldagency.demo.controller.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BorrowRepository borrowRepository;



    @GetMapping(value = "/books?status={status}")
    public ResponseEntity listMyBooks(@RequestParam("status") BookStatus status) {
        Integer userConnectedId = getUserConnectedId();
        List<Book> books;
        if(status != null & status.equals(BookStatus.FREE)) {
            books = bookRepository.findByBookStatusAndUserIdNotAndDeletedFalse(status, userConnectedId);
        } else {
            books = bookRepository.findByUserIdAndDeletedFalse(userConnectedId);
        }
        return new ResponseEntity(books, HttpStatus.OK);
    }

    public static Integer getUserConnectedId() {
        return 1;
    }

    @PostMapping(value = "/books")
    public ResponseEntity addBooks(@Validated @RequestBody Book book) {
        Integer userConnectedId = getUserConnectedId();
        Optional<User> user = userRepository.findById(userConnectedId);
        Optional<Category> category = categoryRepository.findById(book.getCategoryId());
        if(category.isPresent()) {
            book.setCategory(category.get());
        }else {
            return new ResponseEntity("You must provide a valid category", HttpStatus.BAD_REQUEST);
        }

        if(user.isPresent()) {
            book.setUser(user.get());
        }else {
            return new ResponseEntity("You must enter valid user", HttpStatus.BAD_REQUEST);
        }
        book.setDeleted(false);
        book.setBookStatus(BookStatus.FREE);
        bookRepository.save(book);
        return new ResponseEntity(book, HttpStatus.CREATED);
    }

    @DeleteMapping(value="/books/{bookId}")
    public ResponseEntity deleteBook(@PathVariable("bookId") String bookId) {
        Optional<Book> book = bookRepository.findById(Integer.valueOf(bookId));
        if(book.isEmpty()) {
            return new ResponseEntity("The book does not exist", HttpStatus.BAD_REQUEST);
        }
        Book bookToDelete = book.get();
        List<Borrow> borrows = borrowRepository.findByBookId(bookToDelete.getId());
        for(Borrow borrow: borrows) {
            if(borrow.getCloseDate() == null) {
                User borrower = borrow.getBorrower();
                return new ResponseEntity(borrower, HttpStatus.CONFLICT);
            }
        }
        bookToDelete.setDeleted(true);
        bookRepository.save(bookToDelete);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value="/books/{bookId}")
    public ResponseEntity updateBook(@PathVariable("bookId") String bookId, @Validated @RequestBody Book updatedBook) {
        Optional<Book> bookOpt = bookRepository.findById(Integer.valueOf(bookId));
        if(bookOpt.isEmpty()) {
            return new ResponseEntity("Book not existing", HttpStatus.BAD_REQUEST);
        }
        Book bookToSave = bookOpt.get();
        Optional<Category> category = categoryRepository.findById(updatedBook.getCategoryId());

        if(category.isPresent()) {
            bookToSave.setCategory(category.get());
        }else {
            return new ResponseEntity("You must provide a valid category", HttpStatus.BAD_REQUEST);
        }

        bookToSave.setTitle(updatedBook.getTitle());
        bookRepository.save(bookToSave);

        return new ResponseEntity(bookToSave, HttpStatus.OK);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity loadBook(@PathVariable("bookId") String bookId) {
        Optional<Book> bookOpt = bookRepository.findById(Integer.valueOf(bookId));
        if(bookOpt.isEmpty()) {
            return new ResponseEntity("Book not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(bookOpt.get(), HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity listCategories() {
        return new ResponseEntity(categoryRepository.findAll(), HttpStatus.OK);
    }
}
