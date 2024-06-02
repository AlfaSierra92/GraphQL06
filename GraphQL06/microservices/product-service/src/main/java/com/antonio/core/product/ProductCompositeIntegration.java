package com.antonio.core.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.antonio.core.product.Product;
import com.antonio.core.product.ProductService;
import com.antonio.core.product.Review;
import com.antonio.core.product.ReviewService;
import com.antonio.core.product.exceptions.InvalidInputException;
import com.antonio.core.product.exceptions.NotFoundException;
import com.antonio.core.product.http.HttpErrorInfo;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductCompositeIntegration implements ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String reviewServiceUrl;

    @Autowired
    public ProductCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper mapper,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") int reviewServicePort) {

        this.restTemplate = restTemplate;
        this.mapper = mapper;

        reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/graphql";
    }


    public Review createReview(Review body) {
        throw new UnsupportedOperationException("Creating a review via this microservice is not supported. Use the review service directly.");
    }

    public List<Review> getReviews(int productId) {
        try {
            String query = "query { getReviews(productId: " + productId + ") { reviewId productId author subject content } }";
            ResponseEntity<String> response = sendGraphQLRequest(reviewServiceUrl, query, new ParameterizedTypeReference<String>() {
            });

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());

            // Extracting values from JSON
            JsonNode reviewsNode = rootNode.path("data").path("getReviews");
            List<Review> reviews = new ArrayList<>();
            for (JsonNode reviewNode : reviewsNode) {
                int reviewId = reviewNode.path("reviewId").asInt();
                String author = reviewNode.path("author").asText();
                String subject = reviewNode.path("subject").asText();
                String content = reviewNode.path("content").asText();
                reviews.add(new Review(reviewId, productId, author, subject, content));
            }

            // Printing the extracted reviews
            LOG.debug("Received Reviews: {}", reviews);

            return reviews;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteReviews(int productId) {
        throw new UnsupportedOperationException("Deleting a review via this microservice is not supported. Use the review service directly.");
    }

    private <T> ResponseEntity<T> sendGraphQLRequest(String url, String query, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        String requestBody = "{\"query\":\"" + query + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
    }

    private <T> ResponseEntity<T> sendGraphQLRequest(String url, String query, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        String requestBody = "{\"query\":\"" + query + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        // Handle HTTP errors here if needed
        return ex;
    }
}