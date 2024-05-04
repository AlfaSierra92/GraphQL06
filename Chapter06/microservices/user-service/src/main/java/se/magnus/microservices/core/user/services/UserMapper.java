package se.magnus.microservices.core.user.services;

import se.magnus.api.core.user.User;
import se.magnus.microservices.core.user.persistence.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "serviceAddress", ignore = true)
    })
    User entityToApi(UserEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true), @Mapping(target = "version", ignore = true)
    })
    UserEntity apiToEntity(User api);
}
