package GuiBSantos.TaskManager.dto;

import java.time.LocalDateTime;

public record TaskStatusHistoryDTO (
        Long id,
        Long taskId,
        String oldStatus,
        String newStatus,
        Long changedById,
        LocalDateTime changedAt
) {}
