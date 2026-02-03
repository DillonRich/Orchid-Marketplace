package com.orchid.orchid_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.orchid.orchid_marketplace.model.Role;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserService userService;
    
    private User user;
    private UUID userId;
    
    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        
        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setRole(Role.CUSTOMER);
        user.setPasswordHash("hashedPassword");
        user.setIsActive(true);
    }
    
    @Test
    void testGetAllUsers() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        
        List<User> result = userService.getAllUsers();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }
    
    @Test
    void testGetUserById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        Optional<User> result = userService.getUserById(userId);
        
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findById(userId);
    }
    
    @Test
    void testGetUserByIdNullThrowsException() {
        assertThrows(NullPointerException.class, () -> userService.getUserById(null));
    }
    
    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        
        Optional<User> result = userService.getUserByEmail("test@example.com");
        
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }
    
    @Test
    void testGetUsersByRole() {
        List<User> users = List.of(user);
        when(userRepository.findByRole(Role.CUSTOMER)).thenReturn(users);
        
        List<User> result = userService.getUsersByRole(Role.CUSTOMER);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Role.CUSTOMER, result.get(0).getRole());
        verify(userRepository, times(1)).findByRole(Role.CUSTOMER);
    }
    
    @Test
    void testGetUsersByRoleNullThrowsException() {
        assertThrows(NullPointerException.class, () -> userService.getUsersByRole(null));
    }
    
    @Test
    void testSearchUsers() {
        String keyword = "test";
        List<User> users = List.of(user);
        when(userRepository.searchActiveUsers(keyword)).thenReturn(users);
        
        List<User> result = userService.searchUsers(keyword);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).searchActiveUsers(keyword);
    }
    
    @Test
    void testCreateUser() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        user.setPasswordHash("rawPassword");
        User result = userService.createUser(user);
        
        assertNotNull(result);
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void testCreateUserNullThrowsException() {
        assertThrows(NullPointerException.class, () -> userService.createUser(null));
    }
    
    @Test
    void testCreateUserNullEmailThrowsException() {
        user.setEmail(null);
        assertThrows(RuntimeException.class, () -> userService.createUser(user));
    }
    
    @Test
    void testCreateUserDuplicateEmailThrowsException() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        
        assertThrows(RuntimeException.class, () -> userService.createUser(user));
    }
    
    @Test
    void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setEmail("updated@example.com");
        updatedUser.setFullName("Updated Name");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("updated@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        User result = userService.updateUser(userId, updatedUser);
        
        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void testUpdateUserNullIdThrowsException() {
        assertThrows(NullPointerException.class, () -> userService.updateUser(null, user));
    }
    
    @Test
    void testUpdateUserNullDetailsThrowsException() {
        assertThrows(NullPointerException.class, () -> userService.updateUser(userId, null));
    }
}
