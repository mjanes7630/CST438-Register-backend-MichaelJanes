package com.cst438.service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cst438.domain.Course;
import com.cst438.domain.CourseDTOG;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.CourseDTOG.GradeDTO;


public class GradebookServiceMQ extends GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	Queue gradebookQueue;
	
	
	public GradebookServiceMQ() {
		System.out.println("MQ grade book service");
	}
	
	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		
	   EnrollmentDTO enrollmentDTO = new EnrollmentDTO(student_email, student_name, course_id);
	   this.rabbitTemplate.convertAndSend(gradebookQueue.getName(), enrollmentDTO);	   
	   return;
	   
	   /*
	   //??wtf am i even doing here??
		Enrollment enroll = new Enrollment();
      Student student = new Student();
      Course course = new Course();
      //student
      student.setEmail(student_email);
      student.setName(student_name);
      //course
      course.setCourse_id(course_id);
      //enroll
      enroll.setStudent(student);
      

      enrollmentRepository.save(enroll);
	*/
	}
	
	@RabbitListener(queues = "registration-queue")
	@Transactional
	public void receive(CourseDTOG courseDTOG) {
		   
      for(GradeDTO g : courseDTOG.grades) {
         Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(g.student_email, courseDTOG.course_id);
         enrollment.setCourseGrade(g.grade);
         
         enrollmentRepository.save(enrollment);
		
	}
	
	

}}
