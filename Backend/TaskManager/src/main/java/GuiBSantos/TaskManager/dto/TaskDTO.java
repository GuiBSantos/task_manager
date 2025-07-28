package GuiBSantos.TaskManager.dto;

import GuiBSantos.TaskManager.Enum.TaskStatus;

import java.util.List;

public record TaskDTO (
        Long id,
        String title,
        String description,
        TaskStatus status,
        Long assignedToId,
        Long teamId,
        List<TaskHistoryDTO> history
) { }
