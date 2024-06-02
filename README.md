# GraphQL06
A simple demonstration of two Spring Boot microservices GraphQL powered for Distributed Edge programming exam.

### What's in it?
We have two microservices:

1. **Product-service** for product retrieval, insertion and deletion (it exposes *getProduct*, *createProduct* and *deleteProduct*) and for product retrieval, insertion and deletion along with reviews (it exposes *getProductAggregate* (it shows product and reviews), *createProductAggregate* (it creates product with review) and *deleteProductAggregate* (it delete everything related to product)).
2. **Review-service** for review retrieval, insertion and deletion (it exposes *getReviews*, *createReviews* and *deleteReviews*).

Inside the microservices there are been left REST APIs for the same operations, but they are not used in the project (useful just to do comparisons with GraphQL).

Because of the nature of the GraphQL language, there is no OpenAPI-like documentation for the endpoints; available queries and mutations can be retrieved by quering the GraphQL endpoints via an appropriate client, such as Postman.
Anyway, further details on GraphQL can be found [here](https://github.com/AlfaSierra92/GraphQL06/blob/release-rc/GraphQL.md).

All microservices are composed by three layer:
1. **Protocol layer**: it exposes APIs to the users.
2. **Service layer**: it implements the interfaces written in protocol layer.
3. **Persistence layer**: to save data into mongodb.


