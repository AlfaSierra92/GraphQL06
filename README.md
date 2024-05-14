# Microservices with Spring Boot 3 and Spring Cloud, Third Edition - CHAPTER 06
Chapter 06 GraphQL powered for Distributed Edge programming exam.

We have four microservices:

1. **Product-service** (it exposes *getProduct*, *createProduct* and *deleteProduct*).
2. **Recommendation-service** (it exposes *getRecommendations*, *createRecommendations* and *deleteRecommendations*).
3. **Review-service** (it exposes *getReviews*, *createReviews* and *deleteReviews*.)
4. **Product-composite-service** (it exposes *getProductAggregate* in order to watch product along with its own recommendations and reviews, *createProductAggregate* and *deleteProductAggregate*(useless but done to demonstrate how the microservice interfaces with the other)).

*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.*
*This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.*
*You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see http://www.gnu.org/licenses/.*
