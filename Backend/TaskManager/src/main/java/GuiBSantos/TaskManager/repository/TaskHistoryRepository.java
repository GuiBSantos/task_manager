package GuiBSantos.TaskManager.repository;

import GuiBSantos.TaskManager.model.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
}
