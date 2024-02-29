package co.develhope.Exercise13;

import co.develhope.Exercise13.entities.Student;
import co.develhope.Exercise13.repositories.StudentRepository;
import co.develhope.Exercise13.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@SpringBootTest
@ActiveProfiles(value = "test")
public class StudentServiceTest {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;
    @Test
    void checkWorkingStudent() throws Exception{
        Student student = new Student();
        student.setName("Gigi");
        student.setSurname("Proietti");
        student.setWorking(true);
        Student studentDb = studentRepository.save(student);

        assertThat(studentDb.getId()).isNotNull();

        Student studentFromService = studentService.setStudentActivationStatus(student.getId(),true);

        assertThat(studentFromService.getId()).isNotNull();
        assertThat(studentFromService.isWorking()).isTrue();

        Student studentFromFind = studentRepository.findById(studentDb.getId()).get();
        assertThat(studentFromFind).isNotNull();
        assertThat(studentFromFind.getId()).isNotNull();
        assertThat(studentFromFind.getId()).isEqualTo(studentDb.getId());
        assertThat(studentFromFind.isWorking()).isTrue();
    }
}
