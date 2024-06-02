package com.antonio.microservices.core.review.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import com.antonio.microservices.core.review.Review;
import com.antonio.microservices.core.review.ReviewController;
import com.antonio.microservices.core.review.exceptions.InvalidInputException;
import com.antonio.microservices.core.review.persistence.ReviewEntity;
import com.antonio.microservices.core.review.persistence.ReviewRepository;
import com.antonio.microservices.core.review.http.ServiceUtil;

import java.util.List;

@Controller
public class ReviewControllerImpl implements ReviewController {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository repository;

    private final ReviewMapper mapper;

    private final ServiceUtil serviceUtil;

    @Autowired
    public ReviewControllerImpl(ReviewRepository repository, ReviewMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Review createReviews(Review input) {
        try {
            ReviewEntity entity = mapper.apiToEntity(input);
            ReviewEntity newEntity = repository.save(entity);

            LOG.debug("createReview: created a review entity: {}/{}", input.getProductId(), input.getReviewId());
            return mapper.entityToApi(newEntity);

        } catch (DataIntegrityViolationException dive) {
            throw new InvalidInputException("Duplicate key, Product Id: " + input.getProductId() + ", Review Id:" + input.getReviewId());
        }
    }

    @Override
    public List<Review> getReviews(int productId) {

        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        List<ReviewEntity> entityList = repository.findByProductId(productId);
        List<Review> list = mapper.entityListToApiList(entityList);

        LOG.debug("getReviews: response size: {}", list.size());

        return list;
    }

    @Override
    public Boolean deleteReviews(int productId) {
        LOG.debug("deleteReviews: tries to delete reviews for the product with productId: {}", productId);
        repository.deleteAll(repository.findByProductId(productId));

        return Boolean.TRUE;
    }
}