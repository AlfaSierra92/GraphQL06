package se.magnus.microservices.core.user.services;

import se.magnus.api.core.user.User;
import se.magnus.api.core.user.UserService;
import se.magnus.api.exceptions.InvalidInputException;
import se.magnus.api.exceptions.NotFoundException;
import se.magnus.microservices.core.user.persistence.UserEntity;
import se.magnus.microservices.core.user.persistence.UserRepository;
import se.magnus.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final ServiceUtil serviceUtil;

    private final UserRepository repository;

    private final UserMapper mapper;

    int saltLength = 16; // salt length in bytes
    int hashLength = 32; // hash length in bytes
    int parallelism = 1; // currently is not supported
    int memory = 4096; // memory costs
    int iterations = 3;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public User createUser(User body) {
        try {
            body.setName(body.getName().toLowerCase(Locale.ROOT));

            Argon2PasswordEncoder argon2PasswordEncoder =
                    new Argon2PasswordEncoder(saltLength, hashLength, parallelism, memory, iterations);
            body.setPassword(argon2PasswordEncoder.encode(body.getPassword()));
            UserEntity entity = mapper.apiToEntity(body);
            UserEntity newEntity = repository.save(entity);

            log.debug("createProduct: entity created for name: {}", body.getName());
            return mapper.entityToApi(newEntity);

        } catch (DuplicateKeyException dke) {
            throw new InvalidInputException("Duplicate key, Name: " + body.getName());
        }
    }

    @Override
    public User getUser(String name) {

        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("Invalid name: " + name);
        }

        UserEntity entity = repository.findByName(name.toLowerCase())
                .orElseThrow(() -> new NotFoundException("No user found for name: " + name));

        User response = mapper.entityToApi(entity);
        response.setPassword(null);
        response.setServiceAddress(serviceUtil.getServiceAddress());

        log.debug("getProduct: found name: {}", response.getName());

        return response;
    }

    @Override
    public User getUserId(int userId) {

        if (userId == 0 ) {
            throw new InvalidInputException("Invalid userId: " + userId);
        }

        UserEntity entity = repository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No user found for userId: " + userId));

        User response = mapper.entityToApi(entity);
        response.setServiceAddress(serviceUtil.getServiceAddress());
        response.setPassword(null);
        log.debug("getProduct: found userId: {}", response.getUserId());

        return response;
    }

    @Override
    public void deleteUser(int userId) {
        log.debug("deleteProduct: tries to delete an entity with userId: {}", userId);
        repository.findByUserId(userId).ifPresent(e -> repository.delete(e));
    }

    @Override
    public User getAuth(String name) {

        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("Invalid name: " + name);
        }

        UserEntity entity = repository.findByName(name.toLowerCase())
                .orElseThrow(() -> new NotFoundException("No user found for name: " + name));

        User response = mapper.entityToApi(entity);
        //response.setPassword(null);
        response.setServiceAddress(serviceUtil.getServiceAddress());

        log.debug("getName: found name: {}", response.getName());

        return response;
    }
}
