package ovh.kkazm.bugtracker.user.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
// TODO Validation
public record SignUpUserRequest(
        String firstName,
        String lastName,
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
