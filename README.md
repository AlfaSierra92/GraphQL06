# Microservices with Spring Boot 3 and Spring Cloud, Third Edition - CHAPTER 06
Chapter 06 GraphQL powered for Distributed Edge programming exam.

We have four microservices:

1. **Product-service** (it exposes *getProduct*, *createProduct* and *deleteProduct*).
2. **Recommendation-service** (it exposes *getRecommendations*, *createRecommendations* and *deleteRecommendations*).
3. **Review-service** (it exposes *getReviews*, *createReviews* and *deleteReviews*.)
4. **Product-composite-service** (it exposes *getProductAggregate* in order to watch product along with its own recommendations and reviews, *createProductAggregate* and *deleteProductAggregate*(useless but done to demonstrate how the microservice interfaces with the other)).
