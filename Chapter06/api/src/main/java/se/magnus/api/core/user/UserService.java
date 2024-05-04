package se.magnus.api.core.user;

import org.springframework.web.bind.annotation.*;

public interface UserService {

    /**
     * Sample usage, see below.
     *
     * curl -X POST $HOST:$PORT/user \
     *   -H "Content-Type: application/json" --data \
     *   '{
     * 	    "userId": 25,
     *      "name":"google",
     *      "email":"aaaa@aaa.com",
     * 	    "password":"asasasas"
     *    }'
     *
     * @param body A JSON representation of the new user
     * @return A JSON representation of the newly created user
     */
    @PostMapping(
            value = "/user",
            consumes = "application/json",
            produces = "application/json")
    User createUser(@RequestBody User body);

    /**
     * Sample usage: "curl $HOST:$PORT/user/google".
     *
     * @param name of the product
     * @return the user, if found, else null
     */
    @GetMapping(
            value = "/user/{name:[A-Za-z]+}",
            produces = "application/json")
    User getUser(@PathVariable String name);

    /**
     * Sample usage: "curl $HOST:$PORT/user/1".
     *
     * @param userId of the user
     * @return the user, if found, else null
     */
    @GetMapping(
            value = "/user/{userId:[0-9]+}",
            produces = "application/json")
    User getUserId(@PathVariable int userId);

    /**
     * Sample usage: "curl -X DELETE $HOST:$PORT/user/1".
     *
     * @param userId Id of the user
     */
    @DeleteMapping(value = "/user/{userId}")
    void deleteUser(@PathVariable int userId);
}
