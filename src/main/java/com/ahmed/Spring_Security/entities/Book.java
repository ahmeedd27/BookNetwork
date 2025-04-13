package com.ahmed.Spring_Security.entities;

import com.ahmed.Spring_Security.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Book extends BaseEntity {



    private String title;

    private String authorName;

    private String isbn;// the book identifier

    private String synopsis;//small resume about the book

    private String bookCover;

    private boolean archived;

    private boolean shareable;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;

    @OneToMany(mappedBy = "book")
    private List<FeedBack> feedbacks;

    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> bookTransactionHistories;

    @Transient
    public double getRate(){
        if(this.feedbacks==null|| this.feedbacks.isEmpty()){
            return 0.0;
        }
        // now get the rate
        var rate=feedbacks.stream()
                .mapToDouble(FeedBack::getNote)
                .average()
                .orElse(0.0);
        // now make the rate لاقرب رقم عشري
         double rounded=Math.round(rate*10.0)/10.0;
         return rounded;

    }






}
