# GraphQL06
A simple demonstration of four Spring Boot microservices GraphQL powered for Distributed Edge programming exam.

We have four microservices:

1. **Product-service** (it exposes *getProduct*, *createProduct* and *deleteProduct*).
2. **Recommendation-service** (it exposes *getRecommendations*, *createRecommendations* and *deleteRecommendations*).
3. **Review-service** (it exposes *getReviews*, *createReviews* and *deleteReviews*).
4. **Product-composite-service** (it exposes *getProductAggregate*).

Both of microservices is composed by three layer:
1. **Protocol layer** (it exposes APIs to the users).
2. **Service layer** (it implements the interfaces written in protocol layer).
3. **Persistence layer** (to save datas into mongodb).

Further details on GraphQL can be found on this [file](https://github.com/AlfaSierra92/GraphQL06/blob/release/GraphQL.md).
