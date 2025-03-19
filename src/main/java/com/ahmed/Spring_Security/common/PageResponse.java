package com.ahmed.Spring_Security.common;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    // just to see if the page is the first one or the last one
    private boolean first;
    private boolean last;

}
