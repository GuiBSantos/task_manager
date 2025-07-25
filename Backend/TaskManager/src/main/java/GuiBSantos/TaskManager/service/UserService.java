package GuiBSantos.TaskManager.service;

import GuiBSantos.TaskManager.Enum.Role;
import GuiBSantos.TaskManager.dto.UserDTO;
import GuiBSantos.TaskManager.dto.request.UserRegisterDTO;
import GuiBSantos.TaskManager.dto.request.UserUpdateDTO;
import GuiBSantos.TaskManager.model.User;
import GuiBSantos.TaskManager.repository.UserRepository;
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
    private PasswordEncoder passwordEncoder;

    public UserDTO create(UserRegisterDTO dto) {
        validateUserRegisterDTO(dto);

        var user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.valueOf(dto.role()));
        user.setDeleted(false);

        var saved = userRepository.save(user);
        return convertToDTO(saved);
    }

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

    private void validateUserRegisterDTO(UserRegisterDTO dto) {
        if (dto == null) throw new IllegalArgumentException("User data must not be null");
        if (isBlank(dto.name())) throw new IllegalArgumentException("Name is required");
        if (isBlank(dto.email())) throw new IllegalArgumentException("Email is required");
        if (isBlank(dto.password())) throw new IllegalArgumentException("Password is required");
        if (isBlank(dto.role())) throw new IllegalArgumentException("Role is required");
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
                user.getRole().name()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + username + " not found"));
    }
}
