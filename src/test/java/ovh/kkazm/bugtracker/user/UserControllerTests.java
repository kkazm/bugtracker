package ovh.kkazm.bugtracker.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ovh.kkazm.bugtracker.user.dtos.UserDto;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getUserByIdReturnsUserFromService() {
        UserDto user = new UserDto(
                23L,
                "testUser",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                Instant.parse("2026-01-15T10:15:30Z")
        );
        when(userService.get(23L)).thenReturn(user);

        var response = userController.getUserById(23L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(user, response.getBody());
        verify(userService).get(23L);
    }

    @Test
    void deleteProjectByIdDeletesUserAndReturnsNoContent() {
        var response = userController.deleteProjectById(23L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).delete(23L);
    }
}
