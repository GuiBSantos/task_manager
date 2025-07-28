package GuiBSantos.TaskManager.repository;

import GuiBSantos.TaskManager.model.Task;
import GuiBSantos.TaskManager.model.TaskStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskStatusHistoryRepository extends JpaRepository<TaskStatusHistory, Long> {

    List<TaskStatusHistory> findByTaskId(Long taskId);
}
