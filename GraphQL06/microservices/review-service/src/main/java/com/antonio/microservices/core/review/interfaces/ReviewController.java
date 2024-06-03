package com.antonio.microservices.core.review.interfaces;

import com.antonio.microservices.core.review.Review;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.List;

public interface ReviewController {

    @QueryMapping
    public List<Review> getReviews(@Argument int productId);

    @MutationMapping
    public Review createReviews(@Argument Review input);

    @MutationMapping
    public Boolean deleteReviews(@Argument int productId);
}