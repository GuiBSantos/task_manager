package GuiBSantos.TaskManager.repository;

import GuiBSantos.TaskManager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
