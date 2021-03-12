package security3.authorization.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static security3.common.Constants.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = BASE_PATH)
public class TestRestAPIs {

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String userAccess() {
        return ">>> User Contents!";
    }

    @GetMapping("/pm")
    @PreAuthorize("hasRole('ROLE_PM') or hasRole('ROLE_ADMIN')")
    public String projectManagementAccess() {
        return ">>> Project Management Board";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminAccess() {
        return ">>> Admin Contents";
    }
}
