package com.ahmed.Spring_Security.services;


import com.ahmed.Spring_Security.entities.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> getOwnerId(Integer id){

        return ( root ,  query ,criteriaBuilder) ->
              criteriaBuilder.equal(root.get("createdBy") , id);



    }

}
