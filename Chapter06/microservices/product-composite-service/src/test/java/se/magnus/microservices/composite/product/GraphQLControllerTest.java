package se.magnus.microservices.composite.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import se.magnus.api.core.product.Product;
import se.magnus.microservices.composite.product.services.ProductCompositeIntegration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GraphQLControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ProductCompositeIntegration compositeIntegration;

    void setUp() {
        // Mock response
        when(compositeIntegration.getProduct(1))
                .thenReturn(new Product(1, "name", 1, "mock-address"));
    }

    @Test
    public void testGetProductById() throws JsonProcessingException {
        String query = "{ \"query\": \"query GetProductAggregate { getProductAggregate(productId: 1) { name weight productId } }\" }";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(query, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/graphql",
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(200, response.getStatusCodeValue());

        /*String responseBody = response.getBody();
        // Parse the response body as JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);

        // Check if the "data" field exists in the response
        JsonNode dataNode = root.get("data");
        assertEquals(true, dataNode.isObject());

        // Check if the "getProduct" field exists in the data
        JsonNode getProductNode = dataNode.get("getProduct");
        assertEquals(true, getProductNode.isObject());

        // Assert specific fields in the getProduct response
        assertEquals("1", getProductNode.get("productId").asText());
        assertEquals("Sample Product", getProductNode.get("name").asText());
        assertEquals(10, getProductNode.get("weight").asInt());
        assertEquals("123 Main St", getProductNode.get("serviceAddress").asText());*/
        // Parse responseBody and perform assertions as needed
    }
}
