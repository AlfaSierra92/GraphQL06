package com.antonio.microservices.core.review.persistence;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends CrudRepository<ReviewEntity, String> {

    @Transactional(readOnly = true)
    List<ReviewEntity> findByProductId(int productId);
}