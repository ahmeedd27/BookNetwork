package com.ahmed.Spring_Security.dao;

import com.ahmed.Spring_Security.entities.FeedBack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepo extends JpaRepository<FeedBack , Integer> {

    @Query("select f from FeedBack f where f.book.id= :id")
    Page<FeedBack> findAllFeedBackByBookId(Pageable p, Integer id);
}
