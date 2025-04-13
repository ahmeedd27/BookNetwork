package com.ahmed.Spring_Security.controllers;

import com.ahmed.Spring_Security.common.PageResponse;
import com.ahmed.Spring_Security.dao.FeedBackRepo;
import com.ahmed.Spring_Security.services.FeedBackRequest;
import com.ahmed.Spring_Security.services.FeedBackResponse;
import com.ahmed.Spring_Security.services.FeedBackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
@Tag(name="Feedback")
public class FeedBackController {

    private final FeedBackService feedBackService;
    private final FeedBackRepo feedBackRepo;

    @PostMapping
    public ResponseEntity<Integer> saveFeedBack(
          @Valid @RequestBody FeedBackRequest feedBackRequest ,
          Authentication connectedUser
    ){
        return ResponseEntity.ok(feedBackService.saveFeedBack(feedBackRequest , connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedBackResponse>> findAllFeedbacksByBook(
        @PathVariable("book-id") Integer bookId ,
        @RequestParam(name="page" , defaultValue = "0" , required = false) int page ,
        @RequestParam(name="size" , defaultValue = "10" , required = false) int size ,
        Authentication connectedUser
    ){
        return ResponseEntity.ok(feedBackService.findAllFeedbacksByBookId(page , size , connectedUser , bookId));
    }




}
