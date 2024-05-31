# GraphQL06
A simple demonstration of four Spring Boot microservices GraphQL powered for Distributed Edge programming exam.

We have four microservices:

1. **Product-service** for product retrieval, insertion and deletion (it exposes *getProduct*, *createProduct* and *deleteProduct*).
2. **Recommendation-service** for recommendation retrieval, insertion and deletion (it exposes *getRecommendations*, *createRecommendations* and *deleteRecommendations*).
3. **Review-service** for review retrieval, insertion and deletion (it exposes *getReviews*, *createReviews* and *deleteReviews*).
4. **Product-composite-service** to retrieve product details along with its own recommendations list and reviews list (it exposes *getProductAggregate*); there are also available *createProductAggregate* and *deleteProductAggregate* methods created just for testing purpose.

Because of the nature of the GraphQL language, there is no OpenAPI-like documentation for the endpoints; available queries and mutations can be retrieved by quering the GraphQL endpoints via an appropriate client, such as Postman.
Anyway, further details on GraphQL can be found [here](https://github.com/AlfaSierra92/GraphQL06/blob/release/GraphQL.md).

All microservices are composed by three layer:
1. **Protocol layer**: it exposes APIs to the users.
2. **Service layer**: it implements the interfaces written in protocol layer.
3. **Persistence layer**: to save data into mongodb (product and recommendation services) and mysql (only review service).


