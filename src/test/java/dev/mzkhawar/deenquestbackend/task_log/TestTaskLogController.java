package dev.mzkhawar.deenquestbackend.task_log;

import dev.mzkhawar.deenquestbackend.config.JwtService;
import dev.mzkhawar.deenquestbackend.task.Task;
import dev.mzkhawar.deenquestbackend.task.TaskRepository;
import dev.mzkhawar.deenquestbackend.user.Role;
import dev.mzkhawar.deenquestbackend.user.User;
import dev.mzkhawar.deenquestbackend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TestTaskLogController {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TaskLogRepository taskLogRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    User testUser;
    Task testTask;
    TaskLog testTaskLog;
    String jwt;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskLogRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .firstName("John")
                .lastName("Cena")
                .email("john_cena@wwe.com")
                .password(passwordEncoder.encode("asoDS3@21s"))
                .role(Role.USER)
                .build();
        userRepository.save(testUser);

        jwt = jwtService.generateJwt(testUser);

        testTask = Task.builder()
                .name("Test Task")
                .description("Test Description")
                .build();
        taskRepository.save(testTask);

        testTaskLog = TaskLog.builder()
                .task(testTask)
                .user(testUser)
                .completedAt(LocalDateTime.now())
                .build();
        taskLogRepository.save(testTaskLog);
    }

    @Test
    void givenValidTaskLogRequest_whenCreateTaskLog_thenReturnCreatedTaskLogLocation() throws Exception {
        LocalDateTime completedAt = LocalDateTime.now();
        String taskLogRequestJson = """
                {
                    "taskId": "%d",
                    "completedAt": "%s"
                }
                """.formatted(testTask.getId(), completedAt);

        String createdTaskLogLocation = mockMvc.perform(post("/api/v1/task-logs")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskLogRequestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse()
                .getHeader("Location");

        Assertions.assertNotNull(createdTaskLogLocation);
        Long createdTaskLogId = Long.valueOf(createdTaskLogLocation.substring(
                createdTaskLogLocation.lastIndexOf('/') + 1));

        List<TaskLog> taskLogs = taskLogRepository.findAll();
        assertEquals(2, taskLogs.size());

        TaskLog createdTaskLog = taskLogs.stream().filter(taskLog ->
                taskLog.getId().equals(createdTaskLogId)).findFirst().orElseThrow();

        assertEquals(testTask.getId(), createdTaskLog.getTask().getId());
        assertEquals(testUser.getId(), createdTaskLog.getUser().getId());
        assertEquals(completedAt, createdTaskLog.getCompletedAt());
        assertEquals("http://localhost/api/v1/task-logs/" + createdTaskLog.getId(), createdTaskLogLocation);
    }

    // todo: get, put, delete

}
