
package com.hona.module_32.controller;

import com.hona.module_32.config.SecurityConfig;
import com.hona.module_32.exception.DuplicateEmailException;
import com.hona.module_32.exception.StudentNotFoundException;
import com.hona.module_32.model.Student;
import com.hona.module_32.service.StudentService;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = StudentController.class,
        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
)
@Import(SecurityConfig.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    void createStudent_whenValidRequest_returnsCreated() throws Exception {
        Student savedStudent = new Student(1L, "Anu", "anu@example.com");

        when(studentService.createStudent(any())).thenReturn(savedStudent);

        mockMvc.perform(post("/api/students")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Anu",
                                    "email": "anu@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Anu"))
                .andExpect(jsonPath("$.email").value("anu@example.com"));
    }

    @Test
    void createStudent_whenNameIsEmpty_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/students")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "",
                                    "email": "anu@example.com"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Student name is required"));
    }

    @Test
    void createStudent_whenEmailIsEmpty_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/students")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Anu",
                                    "email": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Student email is required"));
    }

    @Test
    void getStudent_whenStudentDoesNotExist_returnsNotFound() throws Exception {
        when(studentService.getStudent(99L))
                .thenThrow(new StudentNotFoundException("Student not found with ID: 99"));

        mockMvc.perform(get("/api/students/99")
                        .with(user("student").roles("USER")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found with ID: 99"));
    }

    @Test
    void createStudent_whenEmailAlreadyExists_returnsConflict() throws Exception {
        when(studentService.createStudent(any()))
                .thenThrow(new DuplicateEmailException("Student email already exists"));

        mockMvc.perform(post("/api/students")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Anu",
                                    "email": "anu@example.com"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Student email already exists"));
    }

    @Test
    void getStudent_whenUserIsNotLoggedIn_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createStudent_whenUserHasNoAdminRole_returnsForbidden() throws Exception {
        mockMvc.perform(post("/api/students")
                        .with(user("student").roles("USER"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Anu",
                                    "email": "anu@example.com"
                                }
                                """))
                .andExpect(status().isForbidden());
    }
    @Test
    void getStudent_whenStudentExists_returnsStudent() throws Exception {
        when(studentService.getStudent(1L))
                .thenReturn(new Student(1L, "Anu", "anu@example.com"));

        mockMvc.perform(get("/api/students/1")
                        .with(user("student").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anu"));
    }
    @Test
    void getAllStudents_returnsStudentList() throws Exception {
        when(studentService.getAllStudents())
                .thenReturn(List.of(
                        new Student(1L, "Anu", "anu@example.com"),
                        new Student(2L, "Rahul", "rahul@example.com")
                ));

        mockMvc.perform(get("/api/students")
                        .with(user("student").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
    @Test
    void updateStudent_whenValidRequest_returnsUpdatedStudent() throws Exception {
        when(studentService.updateStudent(eq(1L), any()))
                .thenReturn(new Student(1L, "Anu", "new@example.com"));

        mockMvc.perform(put("/api/students/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Anu",
                                    "email": "new@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@example.com"));
    }
    @Test
    void deleteStudent_whenStudentExists_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/students/1")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1L);
    }
}
