package GuiBSantos.TaskManager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskHistoryDTO {
    private Long id;
    private Long taskId;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime changedAt;
}
