package com.ahmed.Spring_Security.entities;


import com.ahmed.Spring_Security.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BookTransactionHistory  extends BaseEntity {
    // this is the associassion table between user and book , one user can borrow many books
    // one book can borrowed by many user
    // this is many to many reltionship

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book; // by doing that the mapping is done

    private boolean returned;
    private boolean returnedApproved;

}
