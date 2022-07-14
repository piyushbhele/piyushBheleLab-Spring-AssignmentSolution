package com.gl.student.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gl.student.entity.Student;
import com.gl.student.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {
	 @Autowired
	    private StudentService studentservice;
	    
	 @RequestMapping({ "/home" })
	    public String goHome() {
	        return "home";
	    }
	 
	 @RequestMapping({ "/home2" })
	    public String goHome2() {
	        return "home2";
	    }
	    @RequestMapping("/list")
	    public String findAll( Model model) {
	        List<Student> StudentList = studentservice.findAll();
	        model.addAttribute("stud", StudentList);
	        return "student";
	    }
	    
	    @RequestMapping("/addstudent")
	    public String ShowFormToAdd( Model model) {
	       
	            Student newstud = new Student();
	           
	            model.addAttribute("addOrUpdate", newstud);
	        
	        return "studentform";
	    }
	    
	    @RequestMapping("/updateStudent")
		public String showFormForUpdate(@RequestParam("id") Integer theId,
				Model theModel) {

			// get the student from the service
			Student theStudent = studentservice.findById(theId);


			// set Student as a model attribute to pre-populate the form
			theModel.addAttribute("addOrUpdate", theStudent);

			// send over to our form
			return "studentform";			
		}
	    
	    @PostMapping("/save")
	    public String save(@RequestParam("id")  Integer id, @RequestParam("firstname") String firstname, @RequestParam("lastname")  String lastname, @RequestParam("course")  String course, @RequestParam("country")  String country) {
	        System.out.println("id is" + id+ "first name is" + firstname);
	        Student s ;
	        if (id != 0) {
	            s = studentservice.findById(id);
	            s.setFirstname(firstname);
	            s.setLastname(lastname);
	            s.setCourse(course);
	            s.setCountry(country);
	        }
	        else {
	            s = new Student(firstname, lastname, course,country);
	        }
	        studentservice.save(s);
	        return "redirect:list";
	    }
	    
	    @RequestMapping("/delete")
	    public String deleteStudent(@RequestParam("id") Integer theid) {

			if(theid!=0)
			{
				Student theStudent = studentservice.findById(theid);
				studentservice.delete(theStudent);
			}
			
			// redirect to /Books/list
			return "redirect:/student/list";
	    }
	    
	    @RequestMapping("/search")
	    public String searchBook(@RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname, @RequestParam("course") String course, @RequestParam("country")  String country, Model model) {
	        List<Student> StudentList = studentservice.findByfirstname_lastname_course_country_(firstname, lastname, course, country);
	        if (StudentList.size() != 0) {
	            model.addAttribute("cust", (Object)StudentList);
	        }
	        else {
	            model.addAttribute("error","No Student Found");
	        }
	        return "student";
	    }
	    
	    @RequestMapping("/403")
		public ModelAndView accessDenied(Principal user)
		{
			ModelAndView mv=new ModelAndView();
			
			if(user!=null)
			{
				mv.addObject("msg", "Hi "+user.getName()+", you are not allowed to access this page");			
			}
			else
			{
				mv.addObject("msg", "Hi, you are not allowed to access this page");	
			}
			mv.setViewName("403");
			return mv;
		}
}
