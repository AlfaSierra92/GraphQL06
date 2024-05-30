#!/bin/bash

# Create a recommendation
echo "Recommendation creation..."
response=$(curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation { createRecommendations(input: { recommendationId: 92, productId: 123, author: \"antonio\", rate: 5, content: \"good\" }) { recommendationId productId author rate content } }"}' \
-w "\n%{http_code}" -s)

http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')

if [ "$http_code" -eq 200 ]; then
    # Check if "errors" field is present in the response
    if grep -q '"errors":' <<< "$body"; then
        error_message=$(echo "$body" | grep -o '"message":"[^"]*"' | cut -d':' -f2 | tr -d '"')
        echo "Error creating recommendation: $error_message"
    else
        # Extract JSON values manually
        recommendationId=$(echo "$body" | grep -o '"recommendationId":[0-9]*' | cut -d':' -f2)
        productId=$(echo "$body" | grep -o '"productId":[0-9]*' | cut -d':' -f2)
        content=$(echo "$body" | grep -o '"content":"[^"]*"' | cut -d':' -f2 | tr -d '"')

        echo "Recommendation created successfully:"
        echo "ID: $recommendationId"
        echo "Product ID: $productId"
        echo "content: $content"
        echo "----------TEST OK----------"
    fi
else
    echo "Error creating recommendation. HTTP Code: $http_code"
    echo "Server response:"
    echo "$body"
    echo "TEST FAILED"
    exit 1
fi
echo -e ""

# Get a recommendation
echo "Recommendation retrieval..."
response=$(curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"query { getRecommendations(productId: 123 ) { recommendationId productId author rate content serviceAddress} }"}' \
-w "\n%{http_code}" -s)

http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')

if [ "$http_code" -eq 200 ]; then
    # Check if "errors" field is present in the response
    if grep -q '"errors":' <<< "$body"; then
        error_message=$(echo "$body" | grep -o '"message":"[^"]*"' | cut -d':' -f2 | tr -d '"')
        echo "Error retrieving recommendation: $error_message"
    else
        # Extract JSON values manually
        recommendationId=$(echo "$body" | grep -o '"recommendationId":[0-9]*' | cut -d':' -f2)
        productId=$(echo "$body" | grep -o '"productId":[0-9]*' | cut -d':' -f2)
        content=$(echo "$body" | grep -o '"content":"[^"]*"' | cut -d':' -f2 | tr -d '"')

        echo "Recommendation details:"
        echo "ID: $recommendationId"
        echo "Product ID: $productId"
        echo "content: $content"
        echo "----------TEST OK----------"
    fi
else
    echo "Error retrieving recommendation. HTTP Code: $http_code"
    echo "Server response:"
    echo "$body"
    echo "TEST FAILED"
    exit 1
fi
echo -e ""

# Try to create a recommendation with the same ID
echo "Recommendation creation with existing recommendationId..."
response=$(curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation { createRecommendations(input: { recommendationId: 92, productId: 123, author: \"antonio\", rate: 5, content: \"good\" }) { recommendationId productId author rate content } }"}' \
-w "\n%{http_code}" -s)

http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')

if [ "$http_code" -eq 200 ]; then
    # Check if "errors" field is present in the response
    if grep -q '"errors":' <<< "$body"; then
        error_message=$(echo "$body" | grep -o '"message":"[^"]*"' | cut -d':' -f2 | tr -d '"')
        echo "Error creating recommendation: $error_message"
        echo "----------TEST OK----------"
    else
        # Extract JSON values manually
        recommendationId=$(echo "$body" | grep -o '"recommendationId":[0-9]*' | cut -d':' -f2)
        productId=$(echo "$body" | grep -o '"productId":[0-9]*' | cut -d':' -f2)
        content=$(echo "$body" | grep -o '"content":"[^"]*"' | cut -d':' -f2 | tr -d '"')

        echo "Recommendation created successfully:"
        echo "ID: $recommendationId"
        echo "Product ID: $productId"
        echo "content: $content"
    fi
else
    echo "Error creating recommendation. HTTP Code: $http_code"
    echo "Server response:"
    echo "$body"
    echo "TEST FAILED"
    exit 1
fi
echo -e ""

# Try to delete a recommendation
echo "Recommendation deletion..."
response=$(curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation DeleteRecommendations { deleteRecommendations(productId: 123) }"}' \
-w "\n%{http_code}" -s)

http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')

if [ "$http_code" -eq 200 ]; then
    # Check if "errors" field is present in the response
    if grep -q '"errors":' <<< "$body"; then
        error_message=$(echo "$body" | grep -o '"message":"[^"]*"' | cut -d':' -f2 | tr -d '"')
        echo "Error deleting recommendation: $error_message"
    else
        echo "Recommendation deleted successfully."
        echo "----------TEST OK----------"
    fi
else
    echo "Error deleting recommendation. HTTP Code: $http_code"
    echo "Server response:"
    echo "$body"
    echo "TEST FAILED"
        exit 1
fi
echo -e ""

# Try to create a recommendation with invalid data
echo "Recommendation creation with invalid data; it should fail..."
response=$(curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation { createRecommendations(input: { recommendationId: aaa, productId: aaa, author: \"antonio\", rate: 5, content: \"good\" }) { recommendationId productId author rate content } }"}' \
-w "\n%{http_code}" -s)

http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')

if [ "$http_code" -eq 200 ]; then
    # Check if "errors" field is present in the response
    if grep -q '"errors":' <<< "$body"; then
        error_message=$(echo "$body" | grep -o '"message":"[^"]*"' | cut -d':' -f2 | tr -d '"')
        echo "Error creating recommendation: $error_message"
        echo "----------TEST OK----------"
    else
        # Extract JSON values manually
        recommendationId=$(echo "$body" | grep -o '"recommendationId":[0-9]*' | cut -d':' -f2)
        productId=$(echo "$body" | grep -o '"productId":[0-9]*' | cut -d':' -f2)
        content=$(echo "$body" | grep -o '"content":"[^"]*"' | cut -d':' -f2 | tr -d '"')

        echo "Recommendation created successfully:"
        echo "ID: $recommendationId"
        echo "Product ID: $productId"
        echo "content: $content"
    fi
else
    echo "Error creating recommendation. HTTP Code: $http_code"
    echo "Server response:"
    echo "$body"
    echo "TEST FAILED"
        exit 1
fi
echo -e ""

# Try to create a recommendation with the same ID
echo "Recommendation creation with same recommendationId after deletion (it should work now)..."
response=$(curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation { createRecommendations(input: { recommendationId: 92, productId: 123, author: \"antonio\", rate: 5, content: \"good\" }) { recommendationId productId author rate content } }"}' \
-w "\n%{http_code}" -s)

http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')

if [ "$http_code" -eq 200 ]; then
    # Check if "errors" field is present in the response
    if grep -q '"errors":' <<< "$body"; then
        error_message=$(echo "$body" | grep -o '"message":"[^"]*"' | cut -d':' -f2 | tr -d '"')
        echo "Error creating recommendation: $error_message"
        echo "----------TEST OK----------"
    else
        # Extract JSON values manually
        recommendationId=$(echo "$body" | grep -o '"recommendationId":[0-9]*' | cut -d':' -f2)
        productId=$(echo "$body" | grep -o '"productId":[0-9]*' | cut -d':' -f2)
        content=$(echo "$body" | grep -o '"content":"[^"]*"' | cut -d':' -f2 | tr -d '"')

        echo "Recommendation created successfully:"
        echo "ID: $recommendationId"
        echo "Product ID: $productId"
        echo "content: $content"
        echo "----------TEST OK----------"
    fi
else
    echo "Error creating recommendation. HTTP Code: $http_code"
    echo "Server response:"
    echo "$body"
    echo "TEST FAILED"
        exit 1
fi
echo -e ""

# Try to delete a recommendation
response=$(curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation DeleteRecommendations { deleteRecommendations(productId: 123) }"}' \
-w "\n%{http_code}" -s)

http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')

if [ "$http_code" -eq 200 ]; then
    # Check if "errors" field is present in the response
    if grep -q '"errors":' <<< "$body"; then
        error_message=$(echo "$body" | grep -o '"message":"[^"]*"' | cut -d':' -f2 | tr -d '"')
        echo "Error deleting recommendation: $error_message"
    else
        echo "Recommendation deleted successfully."
        echo "----------TEST OK----------"
    fi
else
    echo "Error deleting recommendation. HTTP Code: $http_code"
    echo "Server response:"
    echo "$body"
    echo "TEST FAILED"
        exit 1
fi
echo -e ""

# Try tp retrieve a recommendation that does not exist
echo "Recommendations retrieval for a non-existing product..."
echo "It should be empty..."
response=$(curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"query { getRecommendations(productId: 1000 ) { recommendationId productId author rate content serviceAddress} }"}' \
-w "\n%{http_code}" -s)

http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')

if [ "$http_code" -eq 200 ]; then
    # Check if "errors" field is present in the response
    if grep -q '"errors":' <<< "$body"; then
        error_message=$(echo "$body" | grep -o '"message":"[^"]*"' | cut -d':' -f2 | tr -d '"')
        echo "Error retrieving recommendation: $error_message"
    else
        # Extract JSON values manually
        recommendationId=$(echo "$body" | grep -o '"recommendationId":[0-9]*' | cut -d':' -f2)
        productId=$(echo "$body" | grep -o '"productId":[0-9]*' | cut -d':' -f2)
        content=$(echo "$body" | grep -o '"content":"[^"]*"' | cut -d':' -f2 | tr -d '"')

        echo "Recommendation details:"
        echo "ID: $recommendationId"
        echo "Product ID: $productId"
        echo "content: $content"
        echo "----------TEST OK----------"
    fi
else
    echo "Error retrieving recommendation. HTTP Code: $http_code"
    echo "Server response:"
    echo "$body"
    echo "TEST FAILED"
        exit 1
fi
echo -e ""

# Try to create three recommendations and return it
echo "Try to create multiple recommendations and return it"
curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation { createRecommendations(input: { recommendationId: 92, productId: 123, author: \"antonio\", rate: 5, content: \"good\" }) { recommendationId productId author rate content } }"}' \
-w "\n%{http_code}" -s
curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation { createRecommendations(input: { recommendationId: 93, productId: 123, author: \"tizio\", rate: 3, content: \"so and so\" }) { recommendationId productId author rate content } }"}' \
-w "\n%{http_code}" -s
curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation { createRecommendations(input: { recommendationId: 94, productId: 123, author: \"caio\", rate: 1, content: \"bad\" }) { recommendationId productId author rate content } }"}' \
-w "\n%{http_code}" -s
echo -e ""
response=$(curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"query { getRecommendations(productId: 123 ) { recommendationId productId author rate content serviceAddress} }"}' \
-w "\n%{http_code}" -s)
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')
echo -e ""
if [ "$http_code" -eq 200 ]; then
    # Check if "errors" field is present in the response
    if grep -q '"errors":' <<< "$body"; then
        error_message=$(echo "$body" | grep -o '"message":"[^"]*"' | cut -d':' -f2 | tr -d '"')
        echo "Error retrieving recommendation: $error_message"
    else
        # Extract JSON values manually
        recommendationId=$(echo "$body" | grep -o '"recommendationId":[0-9]*' | cut -d':' -f2)
        productId=$(echo "$body" | grep -o '"productId":[0-9]*' | cut -d':' -f2)
        content=$(echo "$body" | grep -o '"content":"[^"]*"' | cut -d':' -f2 | tr -d '"')

        echo "Recommendation details:"
        echo "ID: $recommendationId"
        echo "Product ID: $productId"
        echo "content: $content"
        echo "----------TEST OK----------"
    fi
else
    echo "Error retrieving recommendation. HTTP Code: $http_code"
    echo "Server response:"
    echo "$body"
    echo "TEST FAILED"
    exit 1
fi

# Try to delete a recommendation
echo "Cleaning db..."
response=$(curl --location '127.0.0.1:7002/graphql' \
--header 'Content-Type: application/json' \
--data '{"query":"mutation DeleteRecommendations { deleteRecommendations(productId: 123) }"}' \
-w "\n%{http_code}" -s)

http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | sed '$d')

if [ "$http_code" -eq 200 ]; then
    # Check if "errors" field is present in the response
    if grep -q '"errors":' <<< "$body"; then
        error_message=$(echo "$body" | grep -o '"message":"[^"]*"' | cut -d':' -f2 | tr -d '"')
        echo "Error deleting recommendation: $error_message"
    else
        echo "Recommendation deleted successfully."
        echo "----------TEST OK----------"
    fi
else
    echo "Error deleting recommendation. HTTP Code: $http_code"
    echo "Server response:"
    echo "$body"
    echo "TEST FAILED"
        exit 1
fi
echo -e ""