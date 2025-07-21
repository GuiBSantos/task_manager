package GuiBSantos.TaskManager.mapper;

import GuiBSantos.TaskManager.dto.TaskHistoryDTO;
import GuiBSantos.TaskManager.model.TaskHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskHistoryMapper {

    TaskHistoryDTO toDTO(TaskHistory taskHistory);

    TaskHistory toEntity(TaskHistoryDTO dto);
}
