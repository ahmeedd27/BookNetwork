package com.ahmed.Spring_Security.services;

import com.ahmed.Spring_Security.common.PageResponse;
import com.ahmed.Spring_Security.controllers.BookResponse;
import com.ahmed.Spring_Security.dao.BookRepo;
import com.ahmed.Spring_Security.dao.BookTHRepo;
import com.ahmed.Spring_Security.entities.Book;
import com.ahmed.Spring_Security.entities.BookTransactionHistory;
import com.ahmed.Spring_Security.entities.User;

import com.ahmed.Spring_Security.exception.OperationNotPermittedException;
import com.ahmed.Spring_Security.file.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.ahmed.Spring_Security.services.BookSpecification.*;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepo bookRepo;
    private final BookTHRepo bookTHRepo;
    private final BookMapper bookMapper;
    private final FileStorageService fileStorageService;


    public Integer saveBook(BookRequest bookRequest, Authentication currentUser) {
        User user = (User) currentUser.getPrincipal();
        Book book = bookMapper.toBook(bookRequest);
        book.setOwner(user);
        return bookRepo.save(book).getId();

    }

    public BookResponse findById(Integer bookId) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new NoSuchElementException("Not found"));
        return bookRepo.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Not Found"));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending()); // created date in the base entity
        Page<Book> books = bookRepo.findAllDisplayableBooks(pageable, user.getId());// to display the shareable only and except the connected one
        List<BookResponse> bookResponseList = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponseList,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> books = bookRepo.findAll(getOwnerId(user.getId()), pageable);
        List<BookResponse> bookResponseList = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponseList,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );


    }

    public PageResponse<BorrowedBookResponse> findBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookTransactionHistory> allBorrowedBooks=bookTHRepo.findBorrowedBooks(pageable,user.getId());
         List<BorrowedBookResponse> borrowedBooks=allBorrowedBooks.stream()
                 .map(bookMapper::toBorrowedBookResponse)
                 .toList();
        return new PageResponse<>(
                borrowedBooks,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BookTransactionHistory> allBorrowedBooks=bookTHRepo.findReturnedBooks(pageable,user.getId());
        List<BorrowedBookResponse> borrowedBooks=allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                borrowedBooks,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book b=bookRepo.findById(bookId).orElseThrow(() -> new NoSuchElementException("Not Found"));
        User user = (User) connectedUser.getPrincipal();
        // check to make the only owner how update the books
        if(!Objects.equals(b.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot update shareable status");

        }
        b.setShareable(!b.isShareable());
        bookRepo.save(b);
        return bookId;

    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book b=bookRepo.findById(bookId).orElseThrow(() -> new NoSuchElementException("Not Found"));
        User user = (User) connectedUser.getPrincipal();
        // check to make the only owner how update the books
        if(!Objects.equals(b.getOwner().getId() , user.getId())){
            throw new OperationNotPermittedException("You cannot update shareable status");

        }
        b.setArchived(!b.isArchived());
        bookRepo.save(b);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book b=bookRepo.findById(bookId).orElseThrow(() -> new NoSuchElementException("Not Found"));
        if(b.isArchived()|| !b.isShareable()){
            throw new OperationNotPermittedException("The Book Is Not Shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(b.getOwner().getId() , user.getId())){

            throw new OperationNotPermittedException("You cannot borrow your own book");

        }
        final boolean isAlreadyBorrowed=bookTHRepo.isAlreadyBorrowedByUser(bookId , user.getId());
        if(isAlreadyBorrowed){
            throw new OperationNotPermittedException("You cannot borrow this book it is already borrowed");
        }
        BookTransactionHistory bookTransactionHistory=BookTransactionHistory
                .builder()
                .user(user)
                .book(b)
                .returned(false)
                .returnedApproved(false)
                .build();
        return bookTHRepo.save(bookTransactionHistory).getId();

    }

    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book b=bookRepo.findById(bookId).orElseThrow(() -> new NoSuchElementException("Not Found"));
        if(b.isArchived()|| !b.isShareable()){
            throw new OperationNotPermittedException("The Book Is Not Shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(b.getOwner().getId() , user.getId())){

            throw new OperationNotPermittedException("You cannot borrow or return your own book");

        }
        BookTransactionHistory bookTransactionHistory=bookTHRepo.findBookByIdAndUserId(bookId,user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("you did not borrow this book"));
       bookTransactionHistory.setReturned(true);
       return bookTHRepo.save(bookTransactionHistory).getId();

    }

    public Integer approveReturnedBorrowedBook(Integer bookId, Authentication connectedUser) {
        Book b=bookRepo.findById(bookId).orElseThrow(() -> new NoSuchElementException("Not Found"));
        if(b.isArchived()|| !b.isShareable()){
            throw new OperationNotPermittedException("The Book Is Not Shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(b.getOwner().getId() , user.getId())){

            throw new OperationNotPermittedException("You cannot borrow or return your own book");

        }
        BookTransactionHistory bookTransactionHistory=bookTHRepo.findBookByIdAndOwnerId(bookId,user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("the book is not returned yet"));
        bookTransactionHistory.setReturnedApproved(true);
        return bookTHRepo.save(bookTransactionHistory).getId();
    }

    public void uploadBookCoverPicture(
            MultipartFile file
            , Authentication connectedUser
            , Integer bookId)
    {
        Book b=bookRepo.findById(bookId).orElseThrow(() -> new NoSuchElementException("Not Found"));
        User user = (User) connectedUser.getPrincipal();
        var bookCover=fileStorageService.saveFile(file , user.getId());// the id of the user it is because for each user i want to create a folder where i want to upload all the file that belong to this specific user
     b.setBookCover(bookCover);
     bookRepo.save(b);
    }
}
