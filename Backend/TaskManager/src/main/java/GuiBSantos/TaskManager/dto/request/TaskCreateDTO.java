package GuiBSantos.TaskManager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskCreateDTO(
        @NotBlank String title,
        String description,
        Long assignedToId,
        @NotNull Long teamId
) {
}
