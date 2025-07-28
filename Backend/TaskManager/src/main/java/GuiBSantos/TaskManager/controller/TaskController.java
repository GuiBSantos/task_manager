package GuiBSantos.TaskManager.controller;

import GuiBSantos.TaskManager.docs.TaskControllerDocs;
import GuiBSantos.TaskManager.dto.TaskDTO;
import GuiBSantos.TaskManager.dto.request.TaskCreateDTO;
import GuiBSantos.TaskManager.dto.request.TaskUpdateDTO;
import GuiBSantos.TaskManager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/tasks")
public class TaskController implements TaskControllerDocs {

    @Autowired
    private TaskService taskService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<TaskDTO> create(@RequestBody @Valid TaskCreateDTO dto) {
        var created = taskService.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping(
            value = "/team/{teamId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<List<TaskDTO>> findAllByTeam(@PathVariable Long teamId) {
        var tasks = taskService.findAllByTeam(teamId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
        var task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody TaskUpdateDTO dto) {
        var updated = taskService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
