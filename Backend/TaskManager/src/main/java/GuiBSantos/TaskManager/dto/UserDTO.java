package GuiBSantos.TaskManager.dto;

public record UserDTO(
        Long id,
        String name,
        String email,
        String role
) {
    public UserDTO(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
