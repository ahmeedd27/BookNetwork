package com.ahmed.Spring_Security.controllers;

import com.ahmed.Spring_Security.common.PageResponse;
import com.ahmed.Spring_Security.entities.Book;
import com.ahmed.Spring_Security.services.BookRequest;
import com.ahmed.Spring_Security.services.BookService;
import com.ahmed.Spring_Security.services.BorrowedBookResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;

    //save book
    @PostMapping
    public ResponseEntity<Integer> saveBook(
            @Valid @RequestBody BookRequest bookRequest
            , Authentication currentUser // cosider the connected user that he will create or make a book
    ) {
        return ResponseEntity.ok(bookService.saveBook(bookRequest, currentUser));
    }

    // get book by id
    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable("book-id") Integer bookId
    ) {
        return ResponseEntity.ok(bookService.findById(bookId));
    }

    // return all books with pagination and sorting functionality
    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser // because we need to retrieve all the books for all owners except the connected owner and we will implement an endpoint to retrieve the books for the connected owner
    ) {
        return ResponseEntity.ok(bookService.findAllBooks(page, size, connectedUser));
    }

    // get books by owner
    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> getBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, connectedUser));
    }

    // get all borrowed book
    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findBorrowedBooks(page, size, connectedUser));
    }

    // get all returned books
    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> getReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findReturnedBooks(page, size, connectedUser));
    }
    @PatchMapping("/shareable/{book-id}") // used to change information in existing resource
    public ResponseEntity<Integer> updateSharableStatus(
        @PathVariable("book-id") Integer bookId ,
        Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.updateShareableStatus(bookId , connectedUser));
    }
    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("book-id") Integer bookId ,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.updateArchivedStatus(bookId , connectedUser));
    }

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable("book-id") Integer bookId ,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.borrowBook(bookId,connectedUser));
    }

    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBook(
            @PathVariable("book-id") Integer bookId ,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.returnBorrowedBook(bookId , connectedUser));
    }
    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approvedReturnBorrowBook(
            @PathVariable("book-id") Integer bookId ,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(bookService.approveReturnedBorrowedBook(bookId , connectedUser));
    }
     // to upload cover of the book
    @PostMapping(value="/cover/{book-id}" , consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPic(
            @PathVariable("book-id") Integer bookId ,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser
    ) {
        bookService.uploadBookCoverPicture(file, connectedUser , bookId);
        return ResponseEntity.accepted().build();
    }


}
