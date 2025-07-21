package GuiBSantos.TaskManager.mapper;

import GuiBSantos.TaskManager.dto.TaskDTO;
import GuiBSantos.TaskManager.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDTO toDTO(Task task);

    Task toEntity(TaskDTO dto);
}
