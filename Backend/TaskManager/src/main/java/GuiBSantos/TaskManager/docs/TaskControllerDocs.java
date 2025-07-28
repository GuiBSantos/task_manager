package GuiBSantos.TaskManager.docs;

import GuiBSantos.TaskManager.dto.TaskDTO;
import GuiBSantos.TaskManager.dto.request.TaskCreateDTO;
import GuiBSantos.TaskManager.dto.request.TaskUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskControllerDocs {

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task with the provided details.",
            tags = {"Task Management"},
            responses = {
                    @ApiResponse(description = "Task created successfully", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Invalid request data", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<TaskDTO> create(TaskCreateDTO dto);

    @Operation(
            summary = "List all tasks for a team",
            description = "Retrieves a list of tasks associated with a specific team ID.",
            tags = {"Task Management"},
            responses = {
                    @ApiResponse(description = "Tasks retrieved successfully", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Team not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<List<TaskDTO>> findAllByTeam(Long teamId);

    @Operation(
            summary = "Find task by ID",
            description = "Retrieves detailed information of a task by its ID.",
            tags = {"Task Management"},
            responses = {
                    @ApiResponse(description = "Task found", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Task not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<TaskDTO> findById(Long id);

    @Operation(
            summary = "Update a task",
            description = "Updates the details of an existing task by its ID.",
            tags = {"Task Management"},
            responses = {
                    @ApiResponse(description = "Task updated successfully", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Invalid update data", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Task not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<TaskDTO> update(Long id, TaskUpdateDTO dto);

    @Operation(
            summary = "Delete a task",
            description = "Deletes an existing task by its ID (soft delete).",
            tags = {"Task Management"},
            responses = {
                    @ApiResponse(description = "Task deleted successfully", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Task not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> delete(Long id);
}
