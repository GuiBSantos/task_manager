package GuiBSantos.TaskManager.dto.request;

public record UserRegisterDTO(
        String name,
        String email,
        String password,
        String role
) {}
