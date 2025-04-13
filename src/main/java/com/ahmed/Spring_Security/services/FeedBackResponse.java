package com.ahmed.Spring_Security.services;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedBackResponse {
    private Double note;
    private String comment;
    private boolean ownFeedback; //if the feedback is given by the connected user we highlit it with a different color
}
