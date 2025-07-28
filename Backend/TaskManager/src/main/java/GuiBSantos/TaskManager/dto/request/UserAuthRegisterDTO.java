package GuiBSantos.TaskManager.dto.request;

public record UserAuthRegisterDTO(
        String name,
        String email,
        String password,
        String role
) {}
