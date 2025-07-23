package GuiBSantos.TaskManager.dto;

import java.time.LocalDateTime;

public record TaskDTO (
        Long id,
        String title,
        String description,
        LocalDateTime dueDate,
        String status,
        Long userId
) { }
