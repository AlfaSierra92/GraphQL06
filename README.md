# GraphQL06
A simple demonstration of two Spring Boot microservices GraphQL powered for Distributed Edge programming exam.

We have two microservices:

1. **Product-service** (it exposes *getProduct*, *createProduct* and *deleteProduct*).
2. **Recommendation-service** (it exposes *getRecommendations*, *createRecommendations* and *deleteRecommendations*).

Both of microservices is composed by three layer:
1. **Protocol layer** (it exposes APIs to the users).
2. **Service layer** (it implements the interfaces written in protocol layer).
3. **Persistence layer** (to save datas into mongodb).

Further details on GraphQL can be found on this [file](https://github.com/AlfaSierra92/GraphQL06/blob/release/Docs/GraphQL.md).
