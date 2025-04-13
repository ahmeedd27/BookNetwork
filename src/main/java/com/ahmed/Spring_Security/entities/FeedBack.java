package com.ahmed.Spring_Security.entities;

import com.ahmed.Spring_Security.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class FeedBack extends BaseEntity {

    private Double note;
    private String comment;

    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;



}
