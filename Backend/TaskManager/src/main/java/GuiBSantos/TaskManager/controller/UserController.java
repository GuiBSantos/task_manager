package GuiBSantos.TaskManager.controller;

import GuiBSantos.TaskManager.docs.UserControllerDocs;
import GuiBSantos.TaskManager.dto.UserDTO;
import GuiBSantos.TaskManager.dto.request.UserSetTeamDTO;
import GuiBSantos.TaskManager.dto.request.UserUpdateDTO;
import GuiBSantos.TaskManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/users")
public class UserController implements UserControllerDocs {

    @Autowired
    private UserService userService;

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<UserDTO>> findAll() {
        var result = userService.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        var updated = userService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(
            value = "/{id}/team",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserDTO> setTeam(@PathVariable Long id, @RequestBody UserSetTeamDTO dto) {
        UserDTO updatedUser = userService.assignTeam(id, dto);
        return ResponseEntity.ok(updatedUser);
    }
}
