package se.magnus.api.core.recommendation;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import java.util.List;

public interface RecommendationController {

    @QueryMapping
    List<Recommendation> getRecommendations(@Argument int productId);

    @MutationMapping
    Recommendation createRecommendations(@Argument Recommendation input);

    @MutationMapping
    Boolean deleteRecommendations(@Argument int productId);
}
