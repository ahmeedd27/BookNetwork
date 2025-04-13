package com.ahmed.Spring_Security.services;

import com.ahmed.Spring_Security.entities.Book;
import com.ahmed.Spring_Security.entities.FeedBack;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedBackMapper {
    public FeedBack toFeedback(FeedBackRequest feedBackRequest) {
        return FeedBack
                .builder()
                .book(Book.builder()
                        .id(feedBackRequest.bookId())
                        .build()
                )
                .note(feedBackRequest.note())
                .comment(feedBackRequest.comment())
                .build();
    }

    public FeedBackResponse toFeedBackResponse(FeedBack f, Integer id) {
       return FeedBackResponse.builder()
                .comment(f.getComment())
                .note(f.getNote())
                .ownFeedback(Objects.equals(f.getCreatedBy() , id))
                .build();
    }
}
