# GraphQL06
Chapter 06 GraphQL powered for Distributed Edge programming exam.

We have four microservices:

1. **Product-service** (it exposes *getProduct*, *createProduct* and *deleteProduct*).
2. **Recommendation-service** (it exposes *getRecommendations*, *createRecommendations* and *deleteRecommendations*).
3. **Review-service** (it exposes *getReviews*, *createReviews* and *deleteReviews*.)
4. **Product-composite-service** (it exposes *getProductAggregate* in order to watch product along with its own recommendations and reviews, *createProductAggregate* and *deleteProductAggregate*(useless but done to demonstrate how the microservice interfaces with the other)).

<img width="764" alt="image" src="https://github.com/AlfaSierra92/Microservices-with-Spring-Boot-and-Spring-Cloud-Third-Edition/assets/4050967/26de0a13-9937-4203-a404-d9bf64f2b16a">
