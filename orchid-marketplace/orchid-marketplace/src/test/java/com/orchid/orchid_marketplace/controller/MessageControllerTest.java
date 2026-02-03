package com.orchid.orchid_marketplace.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orchid.orchid_marketplace.dto.SendMessageRequest;
import com.orchid.orchid_marketplace.mapper.ConversationMapper;
import com.orchid.orchid_marketplace.model.User;
import com.orchid.orchid_marketplace.service.MessageService;
import com.orchid.orchid_marketplace.service.UserService;

@WebMvcTest(controllers = MessageController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @MockBean
    private ConversationMapper conversationMapper;

    @MockBean
    private UserService userService;

    private User testUser;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        testUser = new User();
        testUser.setId(testUserId);
        testUser.setEmail("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(testUser));
    }

    @Test
    void sendMessage_rejectsMismatchedConversationId() throws Exception {
        UUID routeId = UUID.randomUUID();
        SendMessageRequest req = new SendMessageRequest();
        req.setConversationId(UUID.randomUUID());
        req.setContent("hello");

        mockMvc.perform(post("/api/messages/conversations/" + routeId + "/send")
                .principal(() -> "test@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest());

        verify(messageService, never()).sendMessage(any(), any(), any());
    }
}
