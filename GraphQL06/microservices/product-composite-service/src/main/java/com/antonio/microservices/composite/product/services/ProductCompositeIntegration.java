package com.antonio.microservices.composite.product.services;

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
import com.antonio.api.core.product.Product;
import com.antonio.api.core.product.ProductService;
import com.antonio.api.core.recommendation.Recommendation;
import com.antonio.api.core.recommendation.RecommendationService;
import com.antonio.api.core.review.Review;
import com.antonio.api.core.review.ReviewService;
import com.antonio.api.exceptions.InvalidInputException;
import com.antonio.api.exceptions.NotFoundException;
import com.antonio.util.http.HttpErrorInfo;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    @Autowired
    public ProductCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper mapper,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") int productServicePort,
            @Value("${app.recommendation-service.host}") String recommendationServiceHost,
            @Value("${app.recommendation-service.port}") int recommendationServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") int reviewServicePort) {

        this.restTemplate = restTemplate;
        this.mapper = mapper;

        productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/graphql";
        recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/graphql";
        reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/graphql";
    }

    @Override
    public Product createProduct(Product productInput) {
        try {
            // THANKS cURL FOR THE HELP!!!
            String mutation = "mutation { createProduct(input: { productId: " + productInput.getProductId() +
                    ", name: \\\"" + productInput.getName() + "\\\", weight: " + productInput.getWeight() + " }) " +
                    "{ productId name weight } }";

            ResponseEntity<String> response = sendGraphQLRequest(productServiceUrl, mutation, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());

            // Extracting values from JSON
            JsonNode productNode = rootNode.path("data").path("createProduct");
            int productId = productNode.path("productId").asInt();
            String name = productNode.path("name").asText();
            int weight = productNode.path("weight").asInt();
            String serviceAddress = productNode.path("serviceAddress").asText();

            // Constructing Product object
            Product product = new Product(productId, name, weight, serviceAddress);

            // Printing the created product
            System.out.println("Created Product: " + product);

            return product;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getProduct(int productId) {
        try {
            String query = "query { getProduct(productId: " + productId + ") { productId name weight serviceAddress } }";
            ResponseEntity<String> response = sendGraphQLRequest(productServiceUrl, query, String.class);

            String responseBody = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseBody);
            // Extracting values from JSON
            JsonNode productNode = rootNode.path("data").path("getProduct");
            if (productNode.isMissingNode()) { // In case of a missing product
                throw new NotFoundException("No product found for productId: " + productId);
            }
            String name = productNode.path("name").asText();
            int weight = productNode.path("weight").asInt();
            String serviceAddress = productNode.path("serviceAddress").asText();

            // Constructing Product object
            Product product = new Product(productId, name, weight, serviceAddress);
            LOG.debug("Found a product with id: {}", product.getProductId());
            return product;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            String mutation = "mutation { deleteProduct(productId: " + productId + ") }";
            ResponseEntity<String> response = sendGraphQLRequest(productServiceUrl, mutation, String.class);

            // Log success message
            LOG.debug("Deleted product with ID: {}", productId);
        } catch (HttpClientErrorException ex) {
            // Handle HTTP errors
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NotFoundException("Product with ID " + productId + " not found.");
            } else {
                throw new RuntimeException("Failed to delete product with ID " + productId + ": " + ex.getMessage());
            }
        } catch (Exception ex) {
            // Handle other exceptions
            throw new RuntimeException("Failed to delete product with ID " + productId + ": " + ex.getMessage());
        }
    }

    public Recommendation createRecommendation(Recommendation body) {
        throw new UnsupportedOperationException("Creating a recommendation via GraphQL is not supported.");
    }

    public List<Recommendation> getRecommendations(int productId) {
        try {
            String query = "query { getRecommendations(productId: " + productId + ") { recommendationId productId author rate content } }";
            ResponseEntity<String> response = sendGraphQLRequest(recommendationServiceUrl, query, new ParameterizedTypeReference<String>() {});

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());

            // Extracting values from JSON
            JsonNode recommendationsNode = rootNode.path("data").path("getRecommendations");
            List<Recommendation> recommendations = new ArrayList<>();
            for (JsonNode recommendationNode : recommendationsNode) {
                int recommendationId = recommendationNode.path("recommendationId").asInt();
                String author = recommendationNode.path("author").asText();
                int rate = recommendationNode.path("rate").asInt();
                String content = recommendationNode.path("content").asText();
                String serviceAddress = recommendationNode.path("serviceAddress").asText();
                recommendations.add(new Recommendation(productId, recommendationId, author, rate, content, serviceAddress));
            }

            // Printing the extracted recommendations
            LOG.debug("Received Recommendations: {}", recommendations);

            return recommendations;
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteRecommendations(int productId) {
        throw new UnsupportedOperationException("Deleting recommendations via GraphQL is not supported.");
    }

    public Review createReview(Review body) {
        throw new UnsupportedOperationException("Creating a review via GraphQL is not supported.");
    }

    public List<Review> getReviews(int productId) {
        try {
            String query = "query { getReviews(productId: " + productId + ") { reviewId productId author subject content serviceAddress } }";
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
                String serviceAddress = reviewNode.path("serviceAddress").asText();
                reviews.add(new Review(reviewId, productId, author, subject, content, serviceAddress));
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
        throw new UnsupportedOperationException("Deleting reviews via GraphQL is not supported.");
    }

    private <T> ResponseEntity<T> sendGraphQLRequest_mutation(String url, String query, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        String requestBody = "{\"query\":\"" + query + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
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