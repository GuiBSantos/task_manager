package GuiBSantos.TaskManager.docs;

import GuiBSantos.TaskManager.dto.TeamDTO;
import GuiBSantos.TaskManager.dto.request.TeamCreateDTO;
import GuiBSantos.TaskManager.dto.request.TeamUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TeamControllerDocs {

    @Operation(
            summary = "Create a team",
            description = "Creates a new team with the provided information.",
            tags = {"Team Management"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<TeamDTO> create(TeamCreateDTO dto);

    @Operation(
            summary = "List all teams",
            description = "Retrieves a list of all registered teams.",
            tags = {"Team Management"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<List<TeamDTO>> findAll();

    @Operation(
            summary = "Find team by ID",
            description = "Retrieves a specific team by its ID.",
            tags = {"Team Management"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<TeamDTO> findById(Long id);

    @Operation(
            summary = "Update team",
            description = "Updates an existing team's information by ID.",
            tags = {"Team Management"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<TeamDTO> update(Long id, TeamUpdateDTO dto);

    @Operation(
            summary = "Delete team",
            description = "Deletes an existing team by ID.",
            tags = {"Team Management"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> delete(Long id);
}
