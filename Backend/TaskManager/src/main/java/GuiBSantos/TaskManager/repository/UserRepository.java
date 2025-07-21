package GuiBSantos.TaskManager.repository;

import GuiBSantos.TaskManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
