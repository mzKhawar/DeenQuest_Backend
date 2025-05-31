package dev.mzkhawar.deenquestbackend.auth;

import com.jayway.jsonpath.JsonPath;
import dev.mzkhawar.deenquestbackend.user.Role;
import dev.mzkhawar.deenquestbackend.user.User;
import dev.mzkhawar.deenquestbackend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TestAuthController {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    User testUser;
    String rawPassword = "asDde#233";

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john_cena@wwe.com")
                .password(passwordEncoder.encode(rawPassword))
                .role(Role.USER)
                .build();
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void givenValidRegisterRequest_whenRegister_thenPersistsUserAndReturnsOkWithJwt() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john_doe@myemail.com";
        String password = "s@0-23dsSx";
        String registerRequestJson = """
                {
                    "firstName": "%s",
                    "lastName": "%s",
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(firstName, lastName, email, password);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt", not(emptyString())));

        User user = userRepository.findByEmail(email).orElseThrow();
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertTrue(passwordEncoder.matches(password, user.getPassword()));
    }

    @Test
    void givenMissingFirstAndLastName_whenRegister_thenReturnsBadRequest() throws Exception {
        String email = "john_doe@myemail.com";
        String password = "s@0-23dsSx";
        String registerRequestJson = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequestJson))
                .andExpect(status().isBadRequest());

        assertTrue(userRepository.findByEmail(email).isEmpty());
    }

    @Test
    void givenValidCredentials_whenAuthenticate_thenReturnsOkWithJwt() throws Exception {
        String authenticationRequestJson = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(testUser.getEmail(), rawPassword);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authenticationRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt", not(emptyString())));
    }

    @Test
    void givenIncorrectPassword_whenAuthenticate_thenReturnsForbidden() throws Exception {
        String authenticationRequestJson = """
                {
                    "email": "%s",
                    "password": "wrong-password"
                }
                """.formatted(testUser.getEmail());

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequestJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenValidJwt_whenAccessingSecuredEndpoint_thenReturnsOk() throws Exception {
        String authenticationRequestJson = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(testUser.getEmail(), rawPassword);

        String response = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authenticationRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt", not(emptyString())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String jwt = JsonPath.read(response, "$.jwt");

        mockMvc.perform(get("/api/v1/tasks")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenMissingJwt_whenAccessingSecuredEndpoint_thenReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/"))
                .andExpect(status().isForbidden());
    }

    // todo: expired/invalid jwt
}
