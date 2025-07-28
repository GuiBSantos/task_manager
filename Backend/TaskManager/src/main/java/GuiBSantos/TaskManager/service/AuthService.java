package GuiBSantos.TaskManager.service;

import GuiBSantos.TaskManager.Enum.Role;
import GuiBSantos.TaskManager.dto.TokenDTO;
import GuiBSantos.TaskManager.dto.request.UserAuthLoginDTO;
import GuiBSantos.TaskManager.dto.request.UserAuthRegisterDTO;
import GuiBSantos.TaskManager.exception.RequiredObjectIsNullException;
import GuiBSantos.TaskManager.model.User;
import GuiBSantos.TaskManager.repository.UserRepository;
import GuiBSantos.TaskManager.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<TokenDTO> signIn(UserAuthLoginDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.email(),
                        credentials.password()
                )
        );

        var user = userRepository.findByEmail(credentials.email())
                .orElseThrow(() -> new UsernameNotFoundException("Username " + credentials.email() + " not found"));

        var token = jwtTokenProvider.createAccessToken(user.getEmail(), List.of(String.valueOf(user.getRole())));

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshSignIn(String username, String refreshToken) {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));

        var token = jwtTokenProvider.refreshToken(refreshToken);
        return ResponseEntity.ok(token);
    }

    private String generateHashedPassword(String password) {
        PasswordEncoder encoder = new Pbkdf2PasswordEncoder(
                "", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> encoders = Map.of("pbkdf2", encoder);

        DelegatingPasswordEncoder delegatingEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        delegatingEncoder.setDefaultPasswordEncoderForMatches(encoder);

        return delegatingEncoder.encode(password);
    }

    public UserAuthRegisterDTO create(UserAuthRegisterDTO userDto) {
        if (userDto == null) {
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating a new User!");

        var user = new User();
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setPassword(generateHashedPassword(userDto.password()));
        user.setRole(Role.valueOf(userDto.role()));

        var savedUser = userRepository.save(user);

        return new UserAuthRegisterDTO(
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPassword(),
                savedUser.getRole().toString()
        );
    }
}
