package com.ahmed.Spring_Security.common;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass // do not create table in database , inheritence only the fields there is no any relation in database
// , do not using join , it is
// best practice to use when there is some fields are commanly used in some  classes without any particular relationship
// also used when i have some of the same fields but i do not want to create separate table for them
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    @Column(nullable = false , updatable = false) // not updatable it is just created at specific time
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false) // to perform it only when we to update the lastModifiedDate
    private LocalDateTime lastModifiedDate;
    @CreatedBy
    @Column(nullable = false , updatable = false)
    private Integer createdBy;
    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;

}
