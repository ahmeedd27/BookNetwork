package com.ahmed.Spring_Security.services;

import jakarta.validation.constraints.*;
import lombok.*;


@Builder
public record FeedBackRequest(
        @Positive(message = "200")
        @Min(value = 0, message = "201")
        @Max(value = 5, message = "202")
        Double note,
        @NotNull(message = "203")
        @NotEmpty(message = "203")
        @NotBlank(message = "203")
        String comment,
        @NotNull(message = "204")
        Integer bookId
) {

}
