package GuiBSantos.TaskManager.controller;

import GuiBSantos.TaskManager.docs.AuthControllerDocs;
import GuiBSantos.TaskManager.dto.request.UserAuthRegisterDTO;
import GuiBSantos.TaskManager.dto.request.UserAuthLoginDTO;
import GuiBSantos.TaskManager.service.AuthService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Endpoint")
@RestController
@RequestMapping("v1/api/auth")
public class AuthController implements AuthControllerDocs {

    @Autowired
    AuthService service;

    @PostMapping("/signin")
    @Override
    public ResponseEntity<?> signIn(@RequestBody UserAuthLoginDTO credentials) {

        if(credentialsIsInvalid(credentials)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client request!");

        var token = service.signIn(credentials);

        if(token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client request!");

       return token;
    }

    @PutMapping("/refresh/{username}")
    @Override
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username, @RequestHeader("Authorization") String refreshToken) {

        if(parametersAreInvalid(username, refreshToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client request!");

        var token = service.refreshSignIn(username, refreshToken);

        if(token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Client request!");

        return token;
    }

    @PostMapping(
            value = "/register",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Override
    public ResponseEntity<UserAuthRegisterDTO> create(@RequestBody UserAuthRegisterDTO userDto) {
        var createdUser = service.create(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    private boolean parametersAreInvalid(String username, String refreshToken) {
        return StringUtils.isBlank(username) || StringUtils.isBlank(refreshToken);
    }

    private static boolean credentialsIsInvalid(UserAuthLoginDTO credentials) {
        return credentials == null ||
                StringUtils.isBlank(credentials.password()) ||
                StringUtils.isBlank(credentials.email());
    }
}
