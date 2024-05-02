package fr.ehn.spotifycloneback.usercontext.mapper;

import fr.ehn.spotifycloneback.usercontext.ReadUserDTO;
import fr.ehn.spotifycloneback.usercontext.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ReadUserDTO readUserDTOToUser(User entity);
}
