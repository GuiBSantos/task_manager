package GuiBSantos.TaskManager.dto.request;

public record UserCreateDTO(
        String name,
        String email,
        String password,
        String role,
        Long teamId
) {}
