package com.ahmed.Spring_Security.dao;

import com.ahmed.Spring_Security.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends JpaRepository<Book,Integer> , JpaSpecificationExecutor<Book> {
    @Query("select b from Book b where b.shareable=true and b.archived=false and b.owner.id!= :id")
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer id);
}
