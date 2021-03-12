package security3.authorization;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import security3.authorization.controller.AuthRestAPIs;
import security3.authorization.message.LoginForm;
import security3.authorization.message.SignUpForm;
import security3.authorization.model.Role;
import security3.authorization.model.RoleName;
import security3.authorization.model.User;
import security3.authorization.repository.RoleRepository;
import security3.authorization.repository.UserRepository;
import security3.authorization.security.jwt.JwtProvider;

import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationTest {

    @InjectMocks
    AuthRestAPIs authRestAPIs;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    RoleRepository roleRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtProvider jwtProvider;

    @Mock
    UserDetails userDetails;

    @Test
    public void testUser() {
        Optional<Role> role = Optional.of(new Role(RoleName.ROLE_ADMIN));
        List<String> str = Lists.newArrayList("admin");
        SignUpForm form = new SignUpForm("Nguyen Van b","vanb", "a@gmail.com", str, "123456");
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(role);
        authRestAPIs.registerUser(form);
    }

//    @Test
//    public void userLogin() {
//        LoginForm loginForm = new LoginForm("vana", "123456");
//        authRestAPIs.authenticateUser(loginForm);
////        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getUsername(),
////                loginForm.getPassword()))).thenReturn();
//    }
}
