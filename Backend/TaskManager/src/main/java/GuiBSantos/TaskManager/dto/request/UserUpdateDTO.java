package GuiBSantos.TaskManager.dto.request;

public record UserUpdateDTO(
        String name,
        String email,
        String password,
        String role
) {}
