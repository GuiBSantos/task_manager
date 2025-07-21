package GuiBSantos.TaskManager.service;

import GuiBSantos.TaskManager.dto.AccountCredentialsDTO;
import GuiBSantos.TaskManager.dto.TokenDTO;
import GuiBSantos.TaskManager.repository.UserRepository;
import GuiBSantos.TaskManager.security.JwtTokenProvider;
import GuiBSantos.TaskManager.dto.AccountCredentialsDTO;
import GuiBSantos.TaskManager.dto.TokenDTO;
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

import java.util.HashMap;
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

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(),
                        credentials.getPassword()
                )
        );

        var user = userRepository.findByUsername(credentials.getUsername());
        if(user == null) throw new UsernameNotFoundException("Username " + credentials.getPassword() + " not found");

        var token = jwtTokenProvider.createAccessToken(credentials.getUsername(), user.getRoles());

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshSignIn(String username, String refreshToken) {

        var user = userRepository.findByUsername(username);
        TokenDTO token;

        if (user != null) {
            token = jwtTokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok(token);
    }

    private String generateHashedPassword(String password) {

        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder.encode(password);

    }

    public AccountCredentialsDTO create(AccountCredentialsDTO user) {

        if (user == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one new User!");

        var entity = new User();

        entity.setFullname(user.getFullname());
        entity.setUserName(user.getUsername());
        entity.setPassword(generateHashedPassword(user.getPassword()));

        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);

        var dto = userRepository.save(entity);

        return new AccountCredentialsDTO(dto.getUsername(), dto.getPassword(), dto.getFullname());

    }
}
