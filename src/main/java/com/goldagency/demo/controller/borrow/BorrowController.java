package com.goldagency.demo.controller.borrow;

import com.goldagency.demo.controller.book.Book;
import com.goldagency.demo.controller.book.BookController;
import com.goldagency.demo.controller.book.BookStatus;
import com.goldagency.demo.controller.borrow.Borrow;
import com.goldagency.demo.controller.repository.BookRepository;
import com.goldagency.demo.controller.repository.BorrowRepository;
import com.goldagency.demo.controller.repository.CategoryRepository;
import com.goldagency.demo.controller.repository.UserRepository;
import com.goldagency.demo.controller.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class BorrowController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BorrowRepository borrowRepository;


    @GetMapping(value = "/borrows")
    public ResponseEntity listBorrowsByUserId() {
        List<Borrow> borrows = borrowRepository.findByBorrowerId(BookController.getUserConnectedId());
        return new ResponseEntity(borrows, HttpStatus.OK);
    }

    @PostMapping("/borrows/{bookId}")
    public ResponseEntity createBorrow(@PathVariable("bookId") String bookId) {
        Integer userConnected = BookController.getUserConnectedId();
        User borrower = userRepository.findById(userConnected).get();
        Book book = bookRepository.findById(Integer.valueOf(bookId)).get();
        User lender = book.getUser();

        if(book.getBookStatus().equals(BookStatus.FREE)) {
            Borrow borrow = new Borrow();
            borrow.setBorrower(borrower);
            borrow.setLender(lender);
            borrow.setBook(book);
            borrow.setAskDate(LocalDate.now());

            borrow = borrowRepository.save(borrow);
            return new ResponseEntity(borrow.getId(), HttpStatus.CREATED);
        }
        return new ResponseEntity("Book not available", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/borrows/{borrowId}")
    public ResponseEntity closeBorrow(@PathVariable("borrowId") String borrowId) {
        Optional<Borrow> borrow = borrowRepository.findById(Integer.valueOf(borrowId));
        if(borrow.isEmpty()) {
            return new ResponseEntity("Borrow not valid", HttpStatus.BAD_REQUEST);
        }

        Borrow borrowToDelete = borrow.get();
        borrowToDelete.setCloseDate(LocalDate.now());
        borrowRepository.save(borrowToDelete);

        Book book = borrowToDelete.getBook();
        book.setBookStatus(BookStatus.FREE);
        bookRepository.save(book);

        return new ResponseEntity(HttpStatus.OK);
    }

}
