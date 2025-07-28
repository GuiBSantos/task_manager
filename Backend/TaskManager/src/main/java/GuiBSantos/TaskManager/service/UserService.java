package GuiBSantos.TaskManager.service;

import GuiBSantos.TaskManager.Enum.Role;
import GuiBSantos.TaskManager.dto.UserDTO;
import GuiBSantos.TaskManager.dto.request.UserAuthRegisterDTO;
import GuiBSantos.TaskManager.dto.request.UserSetTeamDTO;
import GuiBSantos.TaskManager.dto.request.UserUpdateDTO;
import GuiBSantos.TaskManager.model.Team;
import GuiBSantos.TaskManager.model.User;
import GuiBSantos.TaskManager.repository.TeamRepository;
import GuiBSantos.TaskManager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .filter(user -> !user.getDeleted())
                .map(this::convertToDTO)
                .toList();
    }

    public UserDTO findById(Long id) {
        var user = findUserById(id);
        if (user.getDeleted()) {
            throw new RuntimeException("User not found with id: " + id);
        }
        return convertToDTO(user);
    }

    public UserDTO update(Long id, UserUpdateDTO dto) {
        validateUserUpdateDTO(dto);

        var user = findUserById(id);
        var currentUser = getCurrentUser();

        if (!canUpdate(currentUser, user)) {
            throw new SecurityException("You are not allowed to update this user");
        }

        user.setName(dto.name());
        user.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (!currentUser.getRole().equals(Role.MEMBRO)) {
            user.setRole(Role.valueOf(dto.role()));
        }

        var updated = userRepository.save(user);
        return convertToDTO(updated);
    }

    public void delete(Long id) {
        var user = findUserById(id);
        var currentUser = getCurrentUser();

        if (!canDelete(currentUser, user)) {
            throw new SecurityException("You are not allowed to delete this user");
        }

        user.setDeleted(true);
        userRepository.save(user);
    }

    @Transactional(rollbackOn = Exception.class)
    public UserDTO assignTeam(Long userId, UserSetTeamDTO dto) {
        if (userId == null || dto == null || dto.id() == null) {
            throw new IllegalArgumentException("UserId and teamId must not be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getDeleted()) {
            throw new RuntimeException("Cannot assign team to a deleted user");
        }

        Team team = teamRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + dto.id()));

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == Role.MEMBRO) {
            if (!currentUser.getId().equals(user.getId())) {
                throw new SecurityException("Members can only change their own team");
            }

            if (user.getTeam() != null && !user.getTeam().getId().equals(team.getId())) {
                throw new SecurityException("Members cannot change their team");
            }
        } else if (currentUser.getRole() != Role.GERENTE && currentUser.getRole() != Role.ADMIN) {
            throw new SecurityException("You do not have permission to assign teams");
        }

        user.setTeam(team);
        User updated = userRepository.save(user);

        return convertToDTO(updated);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("No authenticated user found");
        }

        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found"));
    }

    private boolean canUpdate(User currentUser, User targetUser) {
        if (currentUser.getRole() == Role.ADMIN) return true;
        if (currentUser.getRole() == Role.GERENTE) {
            return targetUser.getRole() == Role.MEMBRO || currentUser.getId().equals(targetUser.getId());
        }
        return currentUser.getId().equals(targetUser.getId());
    }

    private boolean canDelete(User currentUser, User targetUser) {
        return canUpdate(currentUser, targetUser);
    }

    private void validateUserUpdateDTO(UserUpdateDTO dto) {
        if (dto == null) throw new IllegalArgumentException("User update data must not be null");
        if (isBlank(dto.name())) throw new IllegalArgumentException("Name is required");
        if (isBlank(dto.email())) throw new IllegalArgumentException("Email is required");
        if (isBlank(dto.role())) throw new IllegalArgumentException("Role is required");
    }

    private boolean isBlank(String str) {
        return str == null || str.isBlank();
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
