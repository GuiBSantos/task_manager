package GuiBSantos.TaskManager.repository;

import GuiBSantos.TaskManager.model.Task;
import GuiBSantos.TaskManager.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTeamAndStatusNot(Team team, Enum<?> status);

    List<Task> findByTeam(Team team);

    List<Task> findByTeamAndDeletedFalse(Team team);

    Optional<Task> findByIdAndDeletedFalse(Long id);
}
