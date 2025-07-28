package GuiBSantos.TaskManager.dto;

public record UserDTO(
        Long id,
        String name,
        String email,
        String role,
        Long teamId
) {

    public UserDTO(Long id, String name, String email, String role, Long teamId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.teamId = teamId;
    }
}
