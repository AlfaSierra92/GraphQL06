package com.antonio.core.product.services;

import com.antonio.core.product.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;
import com.antonio.core.product.exceptions.InvalidInputException;
import com.antonio.core.product.exceptions.NotFoundException;
import com.antonio.core.product.persistence.ProductEntity;
import com.antonio.core.product.persistence.ProductRepository;
import com.antonio.core.product.http.ServiceUtil;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductControllerImpl implements ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ServiceUtil serviceUtil;

    private final ProductRepository repository;

    private final ProductMapper mapper;

    private ProductCompositeIntegration integration;


    @Autowired
    public ProductControllerImpl(ProductRepository repository, ProductMapper mapper, ServiceUtil serviceUtil,
                                 ProductCompositeIntegration integration) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public Product getProduct(@Argument int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }

        ProductEntity entity = repository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("No product found for productId: " + productId));

        Product response = mapper.entityToApi(entity);

        LOG.debug("getProduct: found productId: {}", response.getProductId());

        return response;
    }

    @Override
    public Product createProduct(@Argument Product input) {
        try {
            ProductEntity entity = mapper.apiToEntity(input);
            ProductEntity newEntity = repository.save(entity);

            LOG.debug("createProduct: entity created for productId: {}", input.getProductId());
            return mapper.entityToApi(newEntity);

        } catch (DuplicateKeyException dke) {
            throw new InvalidInputException("Duplicate key, Product Id: " + input.getProductId());
        }
    }

    @Override
    public Boolean deleteProduct(int productId) {
        LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
        if (repository.findByProductId(productId).isEmpty()) {
            throw new NotFoundException("No product found for productId: " + productId);
        }
        repository.findByProductId(productId).ifPresent(e -> repository.delete(e));
        return Boolean.TRUE;
    }

    @Override
    public ProductAggregate getProductAggregate(@Argument int productId) {

        LOG.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);

        Product product = getProduct(productId);

        List<Review> reviews = integration.getReviews(productId);

        LOG.debug("getCompositeProduct: aggregate entity found for productId: {}", productId);

        return parsingProductAggregate(product, reviews);
    }

    // Create ProductAggregate from the product, recommendations, and reviews to show them all together
    private ProductAggregate parsingProductAggregate(
            Product product,
            List<Review>reviews) {

        // 1. Setup product info
        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        // 2. Copy summary review info, if available
        List<ReviewSummary> reviewSummaries = (reviews == null) ? null :
                reviews.stream()
                        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent()))
                        .collect(Collectors.toList());

        return new ProductAggregate(productId, name, weight, reviewSummaries);
    }
}