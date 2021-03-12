package security3.authorization.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserDTO {
    @NotBlank(message = "Name is Not Blank")
    private String name;

    @NotBlank(message = "Email is Not Blank")
    private String email;

    @NotBlank(message = "User is not blank")
    private String username;

    @NotBlank(message = "Password is not blank")
    @JsonIgnore
    private String password;
}
