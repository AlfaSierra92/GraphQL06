package se.magnus.api.core.product;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public interface ProductController {

    @QueryMapping
    public Product getProduct(@Argument int productId);
}
