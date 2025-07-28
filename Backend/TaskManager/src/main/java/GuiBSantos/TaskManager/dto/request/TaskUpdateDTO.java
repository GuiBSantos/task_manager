package GuiBSantos.TaskManager.dto.request;

import GuiBSantos.TaskManager.Enum.TaskStatus;
import jakarta.validation.constraints.NotBlank;

public record TaskUpdateDTO(
        @NotBlank String title,
        String description,
        TaskStatus status,
        Long assignedToId
) {}
