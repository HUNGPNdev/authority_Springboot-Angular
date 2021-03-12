package security3.authorization.controller;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import security3.authorization.message.LoginForm;
import security3.authorization.message.SignUpForm;
import security3.authorization.model.Role;
import security3.authorization.model.RoleName;
import security3.authorization.model.User;
import security3.authorization.repository.RoleRepository;
import security3.authorization.repository.UserRepository;
import security3.authorization.response.JwtResponse;
import security3.authorization.response.ResponseMessage;
import security3.authorization.security.jwt.JwtProvider;
import security3.common.ErrorMessageJson;
import security3.common.ResponseApi;

import static security3.common.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = BASE_PATH)
@Slf4j
@Tag(name = "Authorization")
public class AuthRestAPIs {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtProvider jwtProvider;

    @Autowired
    AuthRestAPIs(final AuthenticationManager authentication, final UserRepository userRepository,
                 final RoleRepository roleRepository, final PasswordEncoder encoder, final JwtProvider jwtProvider) {
        authenticationManager = authentication;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
    }

//    @PostMapping("/auth/role")
//    public ResponseEntity<?> createRole() {
//        List<Role> roles = new ArrayList<>();
//        roles.add(new Role(RoleName.ROLE_ADMIN));
//        roles.add(new Role(RoleName.ROLE_PM));
//        roles.add(new Role(RoleName.ROLE_USER));
//        for (Role r: roles) {
//            roleRepository.save(r);
//        }
//        return ResponseEntity.ok("Success");
//    }

    @PostMapping("/auth/signin")
    @Operation(summary = "signin")
    @ApiResponses({
            @ApiResponse(code = 200, message = RESPONSE_200, response = ResponseApi.class),
            @ApiResponse(code = 400, message = RESPONSE_400, response = ErrorMessageJson.class),
            @ApiResponse(code = 401, message = RESPONSE_401, response = ErrorMessageJson.class),
            @ApiResponse(code = 403, message = RESPONSE_403, response = ErrorMessageJson.class),
            @ApiResponse(code = 422, message = RESPONSE_422, response = ErrorMessageJson.class),
            @ApiResponse(code = 500, message = RESPONSE_500, response = ErrorMessageJson.class)
    })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        log.info("[POST] /api/auth");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/auth/signup")
    @Operation(summary = "signup")
    @ApiResponses({
            @ApiResponse(code = 200, message = RESPONSE_200, response = ResponseApi.class),
            @ApiResponse(code = 400, message = RESPONSE_400, response = ErrorMessageJson.class),
            @ApiResponse(code = 401, message = RESPONSE_401, response = ErrorMessageJson.class),
            @ApiResponse(code = 403, message = RESPONSE_403, response = ErrorMessageJson.class),
            @ApiResponse(code = 422, message = RESPONSE_422, response = ErrorMessageJson.class),
            @ApiResponse(code = 500, message = RESPONSE_500, response = ErrorMessageJson.class)
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(),  signUpRequest.getEmail(), signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        List<String> strRoles = signUpRequest.getRole();
        List<Role> roles = new ArrayList<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);

                    break;
                case "pm":
                    Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(pmRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
}
