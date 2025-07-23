package GuiBSantos.TaskManager.dto;

import java.time.LocalDateTime;

public record TaskHistoryDTO (
        Long id,
        Long taskId,
        String oldStatus,
        String newStatus,
        LocalDateTime changedAt
) {}
