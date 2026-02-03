package com.orchid.orchid_marketplace.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.orchid.orchid_marketplace.security.JwtUtil;
import com.orchid.orchid_marketplace.security.SpringUserDetailsService;
import com.orchid.orchid_marketplace.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    @SuppressWarnings("unused")
    private UserService userService;

    @MockitoBean
    @SuppressWarnings("unused")
    private JwtUtil jwtUtil;

    @MockitoBean
    @SuppressWarnings("unused")
    private AuthenticationManager authenticationManager;

    @MockitoBean
    @SuppressWarnings("unused")
    private SpringUserDetailsService springUserDetailsService;

    @Test
    void create_withInvalidPayload_returnsBadRequest() throws Exception {
        // Missing required fields / invalid email and short password/fullName
        String payload = "{" +
                "\"email\":\"not-an-email\"," +
                "\"password\":\"123\"," +
                "\"fullName\":\"A\"" +
                "}";

        mockMvc.perform(post("/api/users")
            .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
            .content(payload))
            .andExpect(status().is4xxClientError());
    }
}
