package GuiBSantos.TaskManager.mapper;

import GuiBSantos.TaskManager.dto.UserDTO;
import GuiBSantos.TaskManager.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO dto);
}
