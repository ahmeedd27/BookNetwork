package com.ahmed.Spring_Security.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore // to ignore the serialization of the infinite loop
    private List<User> users;

    @CreatedDate
    @Column(nullable = false , updatable = false)
    private LocalDateTime createDate;
    @org.springframework.data.annotation.LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime LastModifiedDate;
}
