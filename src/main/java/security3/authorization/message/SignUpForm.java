package security3.authorization.message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {
    private String name;

    private String username;

    private String email;

    private List<String> role;

    private String password;
}
