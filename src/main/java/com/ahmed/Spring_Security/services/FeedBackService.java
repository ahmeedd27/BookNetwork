package com.ahmed.Spring_Security.services;

import com.ahmed.Spring_Security.common.PageResponse;
import com.ahmed.Spring_Security.dao.BookRepo;
import com.ahmed.Spring_Security.dao.FeedBackRepo;
import com.ahmed.Spring_Security.entities.Book;
import com.ahmed.Spring_Security.entities.FeedBack;
import com.ahmed.Spring_Security.entities.User;
import com.ahmed.Spring_Security.exception.OperationNotPermittedException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final BookRepo bookRepo;
    private final FeedBackMapper feedBackMapper;
    private final FeedBackRepo feedBackRepo;

    public Integer saveFeedBack( FeedBackRequest feedBackRequest, Authentication connectedUser) {
        Book book=bookRepo.findById(feedBackRequest.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Not Found"));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You cannot give a feedback for and archived or not shareable book");
        }
        // User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You cannot give feedback to your own book");
        }
        FeedBack feedback = feedBackMapper.toFeedback(feedBackRequest);
        return feedBackRepo.save(feedback).getId();

    }

    public PageResponse<FeedBackResponse> findAllFeedbacksByBookId(int page, int size, Authentication connectedUser, Integer bookId) {
        User user=(User) connectedUser.getPrincipal();
        Pageable p= PageRequest.of(page , size);
        Page<FeedBack> feedBacks=feedBackRepo.findAllFeedBackByBookId( p , bookId);
        List<FeedBackResponse> feedBackResponseList=feedBacks.stream()
                .map(f -> feedBackMapper.toFeedBackResponse(f , user.getId()))
                .toList();
        return new PageResponse<>(
          feedBackResponseList ,
          feedBacks.getNumber() ,
          feedBacks.getSize() ,
          feedBacks.getTotalElements() ,
          feedBacks.getTotalPages() ,
          feedBacks.isFirst() ,
          feedBacks.isLast()
         );

    }


}
