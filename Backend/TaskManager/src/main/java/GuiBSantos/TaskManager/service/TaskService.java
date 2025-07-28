package GuiBSantos.TaskManager.service;

import GuiBSantos.TaskManager.Enum.TaskStatus;
import GuiBSantos.TaskManager.dto.TaskDTO;
import GuiBSantos.TaskManager.dto.TaskHistoryDTO;
import GuiBSantos.TaskManager.dto.request.TaskCreateDTO;
import GuiBSantos.TaskManager.dto.request.TaskUpdateDTO;
import GuiBSantos.TaskManager.model.*;
import GuiBSantos.TaskManager.repository.TaskRepository;
import GuiBSantos.TaskManager.repository.TeamRepository;
import GuiBSantos.TaskManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    public TaskDTO create(TaskCreateDTO dto) {
        validateCreateDTO(dto);

        Team team = teamRepository.findById(dto.teamId())
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + dto.teamId()));

        User assignedTo = null;
        if (dto.assignedToId() != null) {
            assignedTo = userRepository.findById(dto.assignedToId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found with id: " + dto.assignedToId()));
        }

        Task task = new Task();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(TaskStatus.TODO);
        task.setTeam(team);
        task.setAssignedTo(assignedTo);
        task.setDeleted(false);

        var saved = taskRepository.save(task);
        return convertToDTO(saved);
    }

    public List<TaskDTO> findAllByTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + teamId));
        return taskRepository.findByTeamAndDeletedFalse(team).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public TaskDTO findById(Long id) {
        Task task = findTaskById(id);
        return convertToDTO(task);
    }

    public TaskDTO update(Long id, TaskUpdateDTO dto) {
        validateUpdateDTO(dto);

        Task task = findTaskById(id);

        task.setTitle(dto.title());
        task.setDescription(dto.description());
        if (dto.status() != null) {
            task.setStatus(dto.status());
        }
        if (dto.assignedToId() != null) {
            User assignedTo = userRepository.findById(dto.assignedToId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found with id: " + dto.assignedToId()));
            task.setAssignedTo(assignedTo);
        } else {
            task.setAssignedTo(null);
        }

        var updated = taskRepository.save(task);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        Task task = findTaskById(id);
        task.setDeleted(true);
        taskRepository.save(task);
    }

    private Task findTaskById(Long id) {
        return taskRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    private void validateCreateDTO(TaskCreateDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Task data must not be null");
        if (isBlank(dto.title())) throw new IllegalArgumentException("Task title is required");
        if (dto.teamId() == null) throw new IllegalArgumentException("Team ID is required");
    }

    private void validateUpdateDTO(TaskUpdateDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Task update data must not be null");
        if (isBlank(dto.title())) throw new IllegalArgumentException("Task title is required");
    }

    private boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    private TaskDTO convertToDTO(Task task) {
        List<TaskHistoryDTO> historyDTOs = task.getStatusHistory() != null
                ? task.getStatusHistory().stream()
                .filter(history -> !Boolean.TRUE.equals(history.getDeleted()))
                .map(this::convertHistoryToDTO)
                .toList()
                : List.of();

        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getAssignedTo() != null ? task.getAssignedTo().getId() : null,
                task.getTeam() != null ? task.getTeam().getId() : null,
                historyDTOs
        );
    }


    private TaskHistoryDTO convertHistoryToDTO(TaskStatusHistory history) {
        return new TaskHistoryDTO(
                history.getId(),
                history.getTask() != null ? history.getTask().getId() : null,
                history.getOldStatus(),
                history.getNewStatus(),
                history.getChangedAt()
        );
    }

}
