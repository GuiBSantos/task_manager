package GuiBSantos.TaskManager.dto;

public record TeamDTO (
        Long id,
        String name
) {
    public TeamDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
