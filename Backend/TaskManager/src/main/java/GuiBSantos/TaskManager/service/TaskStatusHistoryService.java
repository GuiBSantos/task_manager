package GuiBSantos.TaskManager.service;

import GuiBSantos.TaskManager.dto.TaskStatusHistoryDTO;
import GuiBSantos.TaskManager.model.Task;
import GuiBSantos.TaskManager.model.TaskStatusHistory;
import GuiBSantos.TaskManager.model.User;
import GuiBSantos.TaskManager.repository.TaskRepository;
import GuiBSantos.TaskManager.repository.TaskStatusHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskStatusHistoryService {

    private final TaskStatusHistoryRepository historyRepository;
    private final TaskRepository taskRepository;

    public TaskStatusHistoryService(TaskStatusHistoryRepository historyRepository, TaskRepository taskRepository) {
        this.historyRepository = historyRepository;
        this.taskRepository = taskRepository;
    }

    public List<TaskStatusHistoryDTO> getHistoryByTaskId(Long taskId) {
        List<TaskStatusHistory> historyList = historyRepository.findByTaskId(taskId);
        return historyList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TaskStatusHistoryDTO saveHistory(Long taskId, String oldStatus, String newStatus, User changedBy) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        TaskStatusHistory history = new TaskStatusHistory();
        history.setTask(task);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setChangedBy(changedBy);
        history.setChangedAt(LocalDateTime.now());

        TaskStatusHistory saved = historyRepository.save(history);
        return convertToDTO(saved);
    }

    private TaskStatusHistoryDTO convertToDTO(TaskStatusHistory history) {
        return new TaskStatusHistoryDTO(
                history.getId(),
                history.getTask().getId(),
                history.getOldStatus(),
                history.getNewStatus(),
                history.getChangedBy() != null ? history.getChangedBy().getId() : null,
                history.getChangedAt()
        );
    }
}
