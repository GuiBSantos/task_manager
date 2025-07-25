package GuiBSantos.TaskManager.service;

import GuiBSantos.TaskManager.dto.TeamDTO;
import GuiBSantos.TaskManager.dto.request.TeamCreateDTO;
import GuiBSantos.TaskManager.dto.request.TeamUpdateDTO;
import GuiBSantos.TaskManager.model.Team;
import GuiBSantos.TaskManager.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public TeamDTO create(TeamCreateDTO dto) {
        validateCreateDTO(dto);

        if (teamRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Team with this name already exists");
        }

        var team = new Team();
        team.setName(dto.name());
        team.setDeleted(false);
        // TODO | setar company futuramente

        var saved = teamRepository.save(team);
        return convertToDTO(saved);
    }

    public List<TeamDTO> findAll() {
        return teamRepository.findAll().stream()
                .filter(team -> !team.getDeleted())
                .map(this::convertToDTO)
                .toList();
    }

    public TeamDTO findById(Long id) {
        var team = findTeamById(id);
        if (team.getDeleted()) {
            throw new RuntimeException("Team not found with id: " + id);
        }
        return convertToDTO(team);
    }

    public TeamDTO update(Long id, TeamUpdateDTO dto) {
        validateUpdateDTO(dto);

        var team = findTeamById(id);
        if (team.getDeleted()) {
            throw new RuntimeException("Team not found with id: " + id);
        }

        team.setName(dto.name());

        var updated = teamRepository.save(team);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        var team = findTeamById(id);
        if (team.getDeleted()) {
            throw new RuntimeException("Team not found with id: " + id);
        }

        team.setDeleted(true);
        teamRepository.save(team);
    }

    private Team findTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
    }

    private void validateCreateDTO(TeamCreateDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Team data must not be null");
        if (isBlank(dto.name())) throw new IllegalArgumentException("Team name is required");
    }

    private void validateUpdateDTO(TeamUpdateDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Team update data must not be null");
        if (isBlank(dto.name())) throw new IllegalArgumentException("Team name is required");
    }

    private boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    private TeamDTO convertToDTO(Team team) {
        return new TeamDTO(
                team.getId(),
                team.getName()
                // TODO | add company no DTO futuramente
        );
    }
}
