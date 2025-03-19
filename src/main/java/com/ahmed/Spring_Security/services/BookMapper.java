package com.ahmed.Spring_Security.services;

import com.ahmed.Spring_Security.controllers.BookResponse;
import com.ahmed.Spring_Security.entities.Book;
import com.ahmed.Spring_Security.entities.BookTransactionHistory;
import com.ahmed.Spring_Security.file.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMapper {


    public Book toBook(BookRequest bookRequest) {
        return Book
                .builder()
                .id(bookRequest.id())
                .title(bookRequest.title())
                .authorName(bookRequest.authorName())
                .synopsis(bookRequest.synopsis())
                .isbn(bookRequest.isbn())
                .archived(false)
                .shareable(bookRequest.shareable())
                .build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .owner(book.getOwner().getName())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .bookCover(FileUtils.readFileFromLocation(book.getBookCover())) // the getBookCover from th book is
                //  string(file url) and the bookCover from bookResponse is byte array and i will put in it a
                // method that created to read a byte array(file) from a specific location
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponse
                .builder()
                .authorName(history.getBook().getAuthorName())
                .rate(history.getBook().getRate())
                .id(history.getBook().getId())
                .isbn(history.getBook().getIsbn())
                .returned(history.isReturned())
                .returnedApproved(history.isReturnedApproved())
                .build();
    }
}
