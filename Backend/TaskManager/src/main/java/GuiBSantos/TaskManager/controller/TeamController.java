package GuiBSantos.TaskManager.controller;

import GuiBSantos.TaskManager.docs.TeamControllerDocs;
import GuiBSantos.TaskManager.dto.TeamDTO;
import GuiBSantos.TaskManager.dto.request.TeamCreateDTO;
import GuiBSantos.TaskManager.dto.request.TeamUpdateDTO;
import GuiBSantos.TaskManager.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/teams")
public class TeamController implements TeamControllerDocs {

    @Autowired
    private TeamService teamService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<TeamDTO> create(@RequestBody @Valid TeamCreateDTO dto) {
        var created = teamService.create(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<List<TeamDTO>> findAll() {
        var teams = teamService.findAll();
        return ResponseEntity.ok(teams);
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TeamDTO> findById(@PathVariable Long id) {
        var team = teamService.findById(id);
        return ResponseEntity.ok(team);
    }

    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TeamDTO> update(@PathVariable Long id, @RequestBody TeamUpdateDTO dto) {
        var updated = teamService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
