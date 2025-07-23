package GuiBSantos.TaskManager.service;

import GuiBSantos.TaskManager.Enum.Role;
import GuiBSantos.TaskManager.dto.UserDTO;
import GuiBSantos.TaskManager.dto.request.UserCreateDTO;
import GuiBSantos.TaskManager.dto.request.UserUpdateDTO;
import GuiBSantos.TaskManager.model.User;
import GuiBSantos.TaskManager.repository.TeamRepository;
import GuiBSantos.TaskManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO create(UserCreateDTO dto) {
        validateUserCreateDTO(dto);

        var user = buildUserFromCreateDTO(dto);
        var saved = userRepository.save(user);

        return convertToDTO(saved);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public UserDTO findById(Long id) {
        var user = findUserById(id);
        return convertToDTO(user);
    }

    public UserDTO update(Long id, UserUpdateDTO dto) {
        validateUserUpdateDTO(dto);

        var user = findUserById(id);

        updateUserFromUpdateDTO(user, dto);
        var updated = userRepository.save(user);

        return convertToDTO(updated);
    }

    public void delete(Long id) {
        var user = findUserById(id);
        userRepository.delete(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    private void validateUserCreateDTO(UserCreateDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("User data must not be null");
        }
        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (dto.password() == null || dto.password().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (dto.role() == null || dto.role().isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }
    }

    private void validateUserUpdateDTO(UserUpdateDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("User update data must not be null");
        }
        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (dto.role() == null || dto.role().isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }
    }

    private User buildUserFromCreateDTO(UserCreateDTO dto) {
        var user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.valueOf(dto.role()));

        if (dto.teamId() != null) {
            var team = teamRepository.findById(dto.teamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + dto.teamId()));
            user.setTeam(team);
        }

        return user;
    }

    private void updateUserFromUpdateDTO(User user, UserUpdateDTO dto) {
        user.setName(dto.name());
        user.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        user.setRole(Role.valueOf(dto.role()));

        if (dto.teamId() != null) {
            var team = teamRepository.findById(dto.teamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + dto.teamId()));
            user.setTeam(team);
        } else {
            user.setTeam(null);
        }
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getTeam() != null ? user.getTeam().getId() : null
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
    }
}
