package com.user_service.user.user;

import com.user_service.user.dto.user.UserDTO;
import com.user_service.user.entity.Role;
import com.user_service.user.entity.User;
import com.user_service.user.entity.enums.RoleEnum;
import com.user_service.user.mapper.UserMapper;
import com.user_service.user.repository.RoleRepository;
import com.user_service.user.repository.UserRepository;
import com.user_service.user.service.implementations.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private User user;
    private Role memberRole;

    @BeforeEach
    public void setup() {
        userDTO = new UserDTO(
                1L,
                "johndoe",
                "john.doe@example.com",
                "John",
                "Doe",
                RoleEnum.MEMBER,
                "1234567890"
        );

        user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoneNumber("1234567890");

        memberRole = new Role();
        memberRole.setId(1L);
        memberRole.setName(RoleEnum.MEMBER.name());
    }

    @Test
    public void testAddUser() {

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(memberRole));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.addUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.id(), result.id());
        assertEquals(userDTO.username(), result.username());
        assertEquals(userDTO.email(), result.email());

        verify(userMapper, times(1)).toEntity(userDTO);
        verify(roleRepository, times(1)).findByName(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    public void testAddUserWithNullUsername() {
        User userWithoutUsername = new User();
        userWithoutUsername.setFirstName("John");
        userWithoutUsername.setLastName("Doe");
        userWithoutUsername.setEmail("john.doe@example.com");

        when(userMapper.toEntity(userDTO)).thenReturn(userWithoutUsername);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(memberRole));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.addUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.id(), result.id());
        assertEquals(userDTO.username(), result.username());

        verify(userMapper, times(1)).toEntity(userDTO);
        verify(roleRepository, times(1)).findByName(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    public void testAddUserWithNullDTO() {

        assertThrows(IllegalArgumentException.class, () -> userService.addUser(null));
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(memberRole));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(userId, userDTO);

        assertNotNull(result);
        assertEquals(userDTO.id(), result.id());
        assertEquals(userDTO.username(), result.username());
        assertEquals(userDTO.email(), result.email());

        verify(userMapper, times(1)).toEntity(userDTO);
        verify(roleRepository, times(1)).findByName(anyString());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUserNotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDTO.id(), result.get(0).id());
        assertEquals(userDTO.username(), result.get(0).username());
        assertEquals(userDTO.email(), result.get(0).email());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDTO(user);
    }

    @Test
    public void testGenerateDefaultPassword() {
        User userWithoutPassword = new User();
        userWithoutPassword.setFirstName("John");
        userWithoutPassword.setLastName("Doe");

        when(userMapper.toEntity(userDTO)).thenReturn(userWithoutPassword);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(memberRole));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        userService.addUser(userDTO);

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPassword() != null && !savedUser.getPassword().isEmpty()
        ));
    }

    @Test
    public void testGenerateDefaultUsername() {
        User userWithoutUsername = new User();
        userWithoutUsername.setFirstName("John");
        userWithoutUsername.setLastName("Doe");
        userWithoutUsername.setUsername(null);

        when(userMapper.toEntity(userDTO)).thenReturn(userWithoutUsername);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(memberRole));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        userService.addUser(userDTO);

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getUsername() != null && savedUser.getUsername().equals("John_Doe")
        ));
    }
}