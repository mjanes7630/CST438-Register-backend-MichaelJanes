package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController
{
   @Autowired
   CourseRepository courseRepository;
   
   @Autowired
   StudentRepository studentRepository;
   
   @Autowired
   EnrollmentRepository enrollmentRepository;
   
   @Autowired
   GradebookService gradebookService;
   
   @PostMapping("/student/add")
   @Transactional
   public Student addStudent(@RequestParam String email, @RequestParam String name) {
      
      Student studentExist = studentRepository.findByEmail(email);
      if(studentExist != null) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Studnet already exists.");
      }         
      Student student = new Student();
      student.setEmail(email);
      student.setName(name);
      
      return studentRepository.save(student);      
   }
 
   @PostMapping("/student/status")
   @Transactional
public Student setStudentReg(@RequestParam String email, @RequestBody int stsCode) {
      Student student = studentRepository.findByEmail(email);
      if(student == null) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Studnet doesn't exists.");
      }
      student.setStatusCode(stsCode);
      return studentRepository.save(student);
      
   }
}