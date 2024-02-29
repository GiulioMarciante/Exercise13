package co.develhope.Exercise13.controllers;

import co.develhope.Exercise13.entities.Student;
import co.develhope.Exercise13.repositories.StudentRepository;
import co.develhope.Exercise13.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @PostMapping("/")
    public @ResponseBody Student create(@RequestBody Student student){
        return studentRepository.save(student);
    }
    @GetMapping("/get")
    public @ResponseBody List<Student> getList(){
        return studentRepository.findAll();
    }
    @GetMapping("/{id}")
    public @ResponseBody Student getSingle(@PathVariable Long id){
        Optional<Student> student = studentRepository.findById(id);
        if(student.isPresent()){
            return student.get();
        }else{
            return null;
        }
    }
    @PutMapping("/{id}")
    public @ResponseBody Student update(@PathVariable Long id,@RequestBody @NonNull Student student){
        student.setId(id);
        return studentRepository.save(student);
    }
    @PutMapping("/{id}/activation")
    public @ResponseBody Student setUserActive(@PathVariable Long id,@RequestParam("working") boolean working){
        return studentService.setStudentActivationStatus(id,working);
    }
    @DeleteMapping("/delete/{id}")
    public @ResponseBody void delete(@PathVariable Long id){
        studentRepository.deleteById(id);
    }

}
