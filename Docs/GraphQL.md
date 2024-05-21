# GraphQL

## Introduction

GraphQL is a query language for APIs (Application Programming Interfaces), developed by Facebook in 2012 and made open-source in 2015.

Unlike traditional REST APIs, where the client has to make multiple requests to obtain all the information it needs, GraphQL allows the client to specify exactly what data it wants to obtain and from where, in a single request.

With GraphQL, the client sends a query describing the structure of the data it wishes to receive, and the server responds with a JSON containing only the requested data, in the format requested by the client. This approach allows greater flexibility and efficiency, as it reduces network overhead and allows the client to obtain only the data it needs, without unnecessary information.

In addition, GraphQL provides a strong typing system, which allows developers to clearly define the data structure and validate queries at compile time. This leads to better automatic API documentation and greater robustness in client and server code.

Another advantage to using GraphQL is the lack of the need to provide for api versioning to allow, for example, legacy applications to run. This is because this query language only returns the data that’s explicitly requested, so new capabilities can be added via new types and new fields on those types without creating a breaking change.

For example, if you want to obtain information about an individual user, you can send the query:
```
query {
  user(id: "123") {
    id
    name
    email
    age
    posts {
      title
      body
    }
  }
}
```

In this query:

- **`query`** is the keyword indicating that a query is being performed.
- **`user`** is the name of the query endpoint, which may correspond to a function or a field defined in the GraphQL server.
- **`(id: "123")`** specifies the query arguments. In this case, we are querying the information of a user with a specific ID (in our example, "123").
- **`id`**, **`name`**, **`email`**, **`age`** are the required user fields.
- **`posts`** is a field that can be a list of objects, which may contain additional fields such as **`title`** and **`body`**, representing the user's posts.

When this query is executed on the GraphQL server, a JSON object will be returned containing the required information about the user, such as ID, name, email, age and posts, or possibly an error if the user is not found or another problem occurs during the execution of the query. For example:

```json
{
  "data": {
    "user": {
      "id": "123",
      "name": "Mario Rossi",
      "email": "mario@example.com",
      "age": 30,
      "posts": [
        {
          "title": "Il mio primo post",
          "body": "Questo è il corpo del mio primo post."
        },
        {
          "title": "Secondo post",
          "body": "Questo è il corpo del mio secondo post."
        }
      ]
    }
  }
}
```

In this answer:

- We have a JSON object with a key **`data`**, which contains the data required by the query.
- Within the object **`data`**, we have an object **`user`**, which contains information about the requested user.
- The object **`user`** contains the properties **`id`**, **`name`**, **`email`** and **`age`**, which match the information of the user specified in the query.
- The property **`posts`` is an array containing the user's post objects, each with fields **`title`** and **`body`**. In this case, we have two returned posts.

In the above example, the values of all parameters were requested; in case it wants only some of them (e.g., just the name), the answer will be:
```json
{
  "data": {
    "user": {
      "name": "Mario Rossi"
    }
  }
}
```
making the query:
```
query {
  user(id: "123") {
    name
  }
}
```

## Technical explanation

### Schema

The GraphQL schema defines the structure of the data available through a GraphQL API. This schema provides a clear map of the available data types and the relationships between them, enabling developers to understand how to interact with the API and what data can be requested and sent. In fact, it is possible to make a request in advance in order to know what objects and queries are available.

In the GraphQL schema, several data types are defined, including:

1. **Object Types**: They represent objects within the system. For example, an object type could represent a user, a post or any other entity in the system.
2. **Fields**: These are the properties of an object type. Each field has a name and a type. Fields can be scalar (strings, numbers, booleans, etc.) or they can be other object types.
3. **Arguments**: These are the parameters passed to fields to customise the result. For example, a query to obtain information about a user might require an argument such as the user's ID.
4. **Scalar types**: These are the primitive data types, such as strings, numbers, Booleans, etc.
5. **List of types**: Indicates an array of a specific type of data. For example, a list of posts.

Here is a simplified example of what a GraphQL schema might look like:

```graphql
type Product {
    productId: Int!
    name: String!
    weight: Int!
    serviceAddress: [String]
}

input ProductInput {
    productId: Int!
    name: String!
    weight: Int!
}

type Query {
    getProduct(productId: Int!): Product!
}

type Mutation {
    createProduct(input: ProductInput!): Product!
    deleteProduct(productId: Int!): Boolean
}
```

In this example:

- An object type was defined: Product, which represents products in the system.
- Each object type has fields representing the properties of that object; with their respective types (integer, string, etc.). The "!" symbol in a GraphQL schema indicates that a field is mandatory, i.e. it must always have a value when returned by the GraphQL server. If a field has the "!" symbol, it means that it cannot be null and must be included in the query result. The usage of the square brackets around type show that the object returned is a List.
- The **`Input`** is a data type used to define the structure of input parameters for mutations. Mutations are operations that modify or update data in the GraphQL server, such as creating a new user or editing a post.
- The type **`Query`** defines the available read operations (queries), such as getProduct, which returns the details of the product having that specific *productId*.
- The **`Mutation`** in GraphQL are operations that allow data to be modified on the server. Whereas queries are used to read data, mutations allow data to be created, modified or deleted in the system; they take as input the parameters defined within the round brackets and return the values of the type defined after the symbol ":"; again the presence of the symbol "!" symbol means that after the operation is executed, it must return something other than *null*. Mutations are defined within the GraphQL schema just like queries, but are annotated with the type **`Mutation`** instead of **`Query`**.

### Responses

A special feature of GraphQL, unlike REST, is that the status code of the HTTP response is always 200, regardless of whether the request was successful or not. This is because GraphQL handles errors differently from traditional REST APIs.

Instead of using the HTTP status code to report errors, GraphQL returns a JSON object with an 'errors' key in the response body if errors occur during query processing. This object contains a list of errors giving details of the type of error and where it occurred.

For example, if a GraphQL query contains a syntax error, the server will respond with a status 200 and include an 'errors' object in the response to indicate the specific error:

- **Wrong query**:

    ```bash
    query GetProduct {
        getProduct(productId: 123 { # it lacks the ")" symbol after 123
            productId
            name
            weight
            serviceAddress
        }
    }
    ```

- **Response**:

    ```bash
    {
        "errors": [
          {
            "message": "Invalid Syntax : offending token '{' at line 2 column 31"
          }
        ]
     }
    ```


On the other hand, if the query is processed correctly and there are no errors, the response will still be status 200 and will include the requested data in the JSON response.

Therefore, when working with GraphQL, it is important to examine the content of the JSON response to determine whether the request was successful or not, rather than relying solely on the HTTP status code.

**Exceptions handling**

It is possible to enhance the response of wrong query in order to make it more verbose (e.g. *notfoundexception* in the case of an object not found instead of a generic one). It can be done by adding a custom exception resolver class (extending *DataFetcherExceptionResolverAdapter*) like this:
```java
@Component
public class GraphQlCustomExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof NotFoundException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        } else if (ex instanceof InvalidInputException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        } else if (ex instanceof BadRequestException) {
            return GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .build();
        }
        else {
            return null;
        }
    }
}
```
where every exception returns a more verbose error. Obviously each exception will have to be declared separately (see attached code in the repository).

## Spring boot with GraphQL

### Implementation

Spring Boot and GraphQL can be combined to create web services that use GraphQL as a query layer to access data. Spring Boot offers a number of libraries and tools that simplify the integration of GraphQL in Java applications.

In order to implement correctly, there are a few steps to follow:

1. Inclusion of the correct dependencies in Maven or Gradle.
2. The definition of the schema with the various types of objects, inputs, queries and mutations.
3. Enabling the graphql endpoint in the file *application.yml*.
4. The creation of the controller interface and its implementation

### 1. Inclusion of addictions

**Gradle**

In the file *build.gradle* enter *implementation 'org.springframework.boot:spring-boot-starter-graphql'* in order to have it similar to:

```bash
plugins {
    id 'org.springframework.boot' version '2.6.4'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
    id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '11'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-graphql'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

```

**Maven**

In the case of Apache Maven, the file *pom.xml* must contain:
```xml
<dependencies>
    <!-- Other dependencies -->

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-graphql</artifactId>
    </dependency>
</dependencies>

```

### 2. Schema definition.graphqls

In order to define the objects, we must create the file, with the extension `*.graphqls`, which must be placed in the path `src/main/java/resources/graphql`.

There shall be a single schema file within the folder; it is possible to split them in the case of defining different schemas for different applications that draw from the same *resources* folder by creating subfolders and defining this in the respective *application.yml (see later).*

### 3. Enabling graphQL endpoints

It is then necessary to enable the endpoint by entering the correct entries in the file *application.yml*:

```yaml
spring:
  graphql:
    schema:
      locations: classpath*:graphql/**/
```

If desired, it is possible to declare a different classpath in order to place *.graphls files in different subdirectories for reasons of convenience. In that case:

```yaml
spring:
  graphql:
    schema:
      locations: classpath*:graphql/product-service/**/
```

In this case, the application will search for the schema file within the subfolder *product-service.*

By default, the endpoint will be reachable at the url `$ADDRESS:$PORT/graphql`. You can change this by adding in *application.yml*:

```yaml
spring:
       graphql:
               path: /api/projects/graphql
```

### 4. Java interface definition and implementation

- **Controller Interface**:

```java
public interface ProductController {

    @QueryMapping
    public Product getProduct(@Argument int productId);

    @MutationMapping
    public Product createProduct(@Argument Product input);

    @MutationMapping
    public Boolean deleteProduct(@Argument int productId);
}
```

Worthy of note are the annotations *@QueryMapping*, which indicates that the query of the same name defined in the schema will refer to this method, *@MutationMapping* with regard to modification queries and finally *@Argument* which refers to the attributes passed by the query or mutation queries.

- **Interface  implementation**:

```java
@Controller
public class ProductControllerImpl implements ProductController { 
    /// CODE HERE
    @Override
    public Product getProduct(@Argument int productId) {
        // CODE HERE
        return response;
    }
}
```

### Requests
**GraphiQL (for testing purpose)**

Spring boot's GraphQL library provides a browser-accessible default endpoint for composing and testing queries and mutations. It can be enabled via *application.yml* file by adding this:
```yaml
spring:
    graphiql:
        path: /graphiql
        enabled: true
```
It can be changed via *path:* field.

Here is an example:
<img width="1278" alt="image" src="https://github.com/AlfaSierra92/GraphQL06/assets/4050967/de34745f-bbbf-469c-86b9-58fbd171083c">
It is advisable to use this mode only during development, as enabling this endpoint could potentially create security issues.


**Postman**

With Postman, this is very simple, as it is already set up for GraphQL queries. By putting in the correct url, it will automatically retrieve the schema of objects, queries and mutations and allow the various queries to be composed intuitively.

![Postman example](Postman.png)

**cURL**

With it, the composition of requests is more laborious as one has to compose requests by hand:

- **Query di interrogazione**:

    ```bash
    curl --location '127.0.0.1:7001/graphql' \
    --header 'Content-Type: application/json' \
    --data '{"query":"query GetProduct { getProduct(productId: 92) { productId name weight serviceAddress } }"}'
    ```

- **Query di inserimento (*mutation*)**:

    ```bash
    curl --location '127.0.0.1:7001/graphql' \
    --header 'Content-Type: application/json' \
    --data '{"query":"mutation { createProduct(input: { productId: 92, name: \"1111\", weight: 111 }) { productId name weight } }"}'
    ```

**By code**

Since the response of a GraphQL query is nothing more than a json-formatted body, you can parse it as you have always done in the case of REST.
Here is an example of Java code:
```java
@Override
    public Product getProduct(int productId) {
        try {
            // Query building
            String query = "query { getProduct(productId: " + productId + ") { productId name weight serviceAddress } }";
            // Query execution (the method is declared later)
            ResponseEntity<String> response = sendGraphQLRequest(productServiceUrl, query, String.class);

            // Waiting for response
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
// Various exceptions handling
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

// Creating an http request, with the GraphQL query into the body
private <T> ResponseEntity<T> sendGraphQLRequest(String url, String query, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        String requestBody = "{\"query\":\"" + query + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
    }
```

## Resources

- [GraphQL](https://graphql.org/)
- [Spring for GraphQL](https://spring.io/projects/spring-graphql)
- [Building a GraphQL service](https://spring.io/guides/gs/graphql-server)
- [Getting Started with GraphQL and Spring Boot](https://www.baeldung.com/spring-graphql)
- [GraphQL vs REST](https://aws.amazon.com/it/compare/the-difference-between-graphql-and-rest/)
