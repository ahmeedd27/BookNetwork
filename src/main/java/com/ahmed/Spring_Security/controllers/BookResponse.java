package com.ahmed.Spring_Security.controllers;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private Integer id;
    private String title;
    private String authorName;
    private String isbn;// the book identifier
    private String synopsis;//small resume about the book
    private String owner;
    private byte[] bookCover;
    private double rate;
    private boolean archived;
    private boolean shareable;

}
