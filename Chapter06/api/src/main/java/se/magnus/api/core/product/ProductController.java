package se.magnus.api.core.product;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;


public interface ProductController {

    @QueryMapping
    public Product getProduct(@Argument int productId);

    @MutationMapping
    public Product createProduct(@Argument Product input);

    @MutationMapping
    public Boolean deleteProduct(@Argument int productId);
}