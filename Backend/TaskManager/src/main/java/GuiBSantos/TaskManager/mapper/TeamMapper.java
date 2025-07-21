package GuiBSantos.TaskManager.mapper;

import GuiBSantos.TaskManager.dto.TeamDTO;
import GuiBSantos.TaskManager.model.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDTO toDTO(Team team);

    Team toEntity(TeamDTO dto);
}
