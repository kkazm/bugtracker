package ovh.kkazm.bugtracker.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import ovh.kkazm.bugtracker.exceptions.ResourceNotFoundException;
import ovh.kkazm.bugtracker.security.JwtService;
import ovh.kkazm.bugtracker.user.dtos.UserDto;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getReturnsMappedUserWhenUserExists() {
        long userId = 23L;
        User user = new User();
        user.setId(userId);
        UserDto expected = new UserDto(
                userId,
                "testUser",
                List.of(),
                Instant.parse("2026-01-15T10:15:30Z")
        );
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(expected);

        UserDto actual = userService.get(userId);

        assertSame(expected, actual);
        verify(userRepository).findById(userId);
        verify(userMapper).toUserDto(user);
    }

    @Test
    void getThrowsWhenUserDoesNotExist() {
        long userId = 23L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.get(userId)
        );

        assertEquals("User", exception.getResource());
        assertEquals(userId, exception.getId());
        verifyNoInteractions(userMapper);
    }

    @Test
    void deleteRemovesExistingUser() {
        long userId = 23L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        userService.delete(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteThrowsWhenUserDoesNotExist() {
        long userId = 23L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.delete(userId)
        );

        assertEquals("User", exception.getResource());
        assertEquals(userId, exception.getId());
        verify(userRepository, never()).deleteById(userId);
    }
}
