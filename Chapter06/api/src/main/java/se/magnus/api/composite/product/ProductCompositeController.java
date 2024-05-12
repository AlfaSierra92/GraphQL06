package se.magnus.api.composite.product;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

public interface ProductCompositeController {

    @QueryMapping
    public ProductAggregate getProductAggregate(@Argument int productId);

    /*@MutationMapping
    public ProductAggregate createProductAggregate(@Argument ProductAggregate body);

    @MutationMapping
    public Boolean deleteProductAggregate(@Argument int productId);*/
}
