package GuiBSantos.TaskManager.mapper;

import GuiBSantos.TaskManager.dto.UserDTO;
import GuiBSantos.TaskManager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "team.id", target = "team")
    UserDTO toDTO(User user);

    @Mapping(target = "team", ignore = true)
    User toEntity(UserDTO dto);
}
