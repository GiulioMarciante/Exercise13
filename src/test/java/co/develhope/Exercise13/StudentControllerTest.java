package co.develhope.Exercise13;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import co.develhope.Exercise13.controllers.StudentController;
import co.develhope.Exercise13.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class StudentControllerTest {
    @Autowired
    private StudentController studentController;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoad() {
        assertThat(studentController).isNotNull();
    }

    private Student createStudent() throws Exception {
        Student student = new Student(1l, "Giulio", "Marciante", false);

        //converte in JSON
        String studentJSON = objectMapper.writeValueAsString(student);

        //
        MvcResult result = this.mockMvc.perform(post("/student/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
    }

    private Student getStudentFromId(Long id) throws Exception {
        MvcResult result = this.mockMvc.perform(get("/student/" + id))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();
        try {
            String studentJSON = result.getResponse().getContentAsString();
            return objectMapper.readValue(studentJSON, Student.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    void createStudentTest() throws Exception {
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();
    }

    @Test
    void studentById() throws Exception {
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();

        Student studentResponse = getStudentFromId(student.getId());
        assertThat(studentResponse.getId()).isEqualTo(student.getId());
    }

    @Test
    void deleteStudentById() throws Exception {
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/student/delete/" + student.getId())).andDo(print())
                .andExpect(status().isOk()).andReturn();
        Student studentFromResponse = getStudentFromId(student.getId());
        assertThat(studentFromResponse).isNull();
    }

    @Test
    void updateStudentWorking() throws Exception {
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();

        MvcResult result = this.mockMvc.perform(put("/student/" + student.getId() + "/activation?working=true")).andDo(print())
                .andExpect(status().isOk()).andReturn();

        Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
        assertThat(studentFromResponse.isWorking()).isEqualTo(true);
    }
}

