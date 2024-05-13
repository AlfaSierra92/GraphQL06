package se.magnus.api.composite.product;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

public interface ProductCompositeController {

    /**
     * For sample usage, please use Postman.
     * Anyway, you can put the GraphQL query into the body of a POST request.
     * Removed some optional fields to make it more readable.
     *
     * curl -X POST $HOST:$PORT/product-composite \
     *   -H "Content-Type: application/json" --data \
     *   'query GetProductAggregate {
     *     getProductAggregate(productId: 123) {
     *         name
     *         weight
     *         ...
     *         recommendations {
     *             ...
     *         }
     *         reviews {
     *             ...
     *         }
     *         serviceAddresses {
     *             ...
     *         }
     *     }
     * }'
     *
     * @param body A JSON representation of the query for an existing composite product
     */
    @QueryMapping
    public ProductAggregate getProductAggregate(@Argument int productId);

    /*@MutationMapping
    public ProductAggregate createProductAggregate(@Argument ProductAggregate body);

    @MutationMapping
    public Boolean deleteProductAggregate(@Argument int productId);*/
}
