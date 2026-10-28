package ovh.kkazm.bugtracker.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
// TODO Validation
public record SignUpUserRequest(
        String firstName,
        String lastName,
        @NotNull
        @NotBlank
        String username,
        String password
) {
}
