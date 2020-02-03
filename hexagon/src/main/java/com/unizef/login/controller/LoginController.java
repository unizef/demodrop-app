package com.unizef.login.controller;

import javax.validation.Valid;

import com.unizef.login.model.User;
import com.unizef.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

// Democontroller
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unizef.login.model.Demofile;
import com.unizef.login.service.DemoFilesService;

// Home import
import java.util.List;
import java.util.Set;

import com.unizef.login.model.Role;
import org.springframework.ui.Model;


// From ResourceController
import com.unizef.login.model.FileVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "/Users/unizef/Desktop/drops";

    @Autowired
    private DemoFilesService demoFilesService; 
        

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }


    @RequestMapping(value = "/admin/registrationEmployee", method = RequestMethod.POST)
    public ModelAndView newEmployee(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a Employee registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin/home");
        } else {
            userService.saveEmployee(user);
            modelAndView.addObject("successMessage", "Employee has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("admin/home");

        }
    

        return modelAndView;
    }

    @RequestMapping(value="/checkhome", method = RequestMethod.GET)
    public ModelAndView home(Model model){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");

        boolean adminFlag= false; 
        boolean userFlag= false; 
        boolean employeeFlag= false; 

        Set<Role> roles = user.getRoles();

        for(Role role:roles) {
        	
        		adminFlag= "ADMIN".equals(role.getRole());
                userFlag= "USER".equals(role.getRole());
                employeeFlag= "EMPLOYEE".equals(role.getRole());
        	
        }
        
        String name=user.getName().toString();

       if(employeeFlag) {
    	   List<Demofile> list = demoFilesService.getDemofileList();
           model.addAttribute("demofiles", list);
           modelAndView.setViewName("employee/home");
          
       }
        if(userFlag) {
           List<Demofile> list = demoFilesService.getDemofileListByUserId(user.getId());           
           model.addAttribute("demofiles", list);
           modelAndView.setViewName("user/home");
           
       }
        if(adminFlag) {
                 
            modelAndView.addObject("user", new User());
       
            modelAndView.setViewName("admin/home");
        } 
        
        return modelAndView;
    }
        
      
    @RequestMapping(path = { "/download/{id}"})
    public ResponseEntity<InputStreamResource> downloadFileFromLocal(@PathVariable("id") Integer id) {
    	
    	Demofile pdemofile  = demoFilesService.getDemofileOnly(id);
    	
    	 File file = new File(pdemofile.getFilelocation());
         InputStreamResource resource=null;
		try {
			resource = new InputStreamResource(new FileInputStream(file));
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}

         return ResponseEntity.ok()
               .header(HttpHeaders.CONTENT_DISPOSITION,
                     "attachment;filename=" + file.getName())
               .contentType(MediaType.APPLICATION_PDF).contentLength(file.length())
               .body(resource);
    }


    @RequestMapping(path = { "/employee/approval/{id}"})
    public String approveFile(Model model, @PathVariable("id") Integer id) 
                             
    {
    	 ModelAndView modelAndView = new ModelAndView();
    	 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         User user = userService.findUserByEmail(auth.getName());
  
    	Demofile demofile  = demoFilesService.getDemofileOnly(id);
		
        demofile.setStatus("Approved");
       
        model.addAttribute("demofile", demofile);
        demoFilesService.saveDemofile(demofile);

        
        return "redirect:/checkhome";
    }

    // updated 

    @RequestMapping(path = { "/employee/reject/{id}/{comment}"})
    public String rejectFile(Model model, @PathVariable("id") Integer id,@PathVariable("comment") String comment)                              
    {
    	 ModelAndView modelAndView = new ModelAndView();
    	 
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         User user = userService.findUserByEmail(auth.getName());
  
    	Demofile demofile  = demoFilesService.getDemofileOnly(id);
    	
        demofile.setStatus("Rejected");
        demofile.setComments(comment);
    	model.addAttribute("demofile", demofile);
        demoFilesService.saveDemofile(demofile);
        return "redirect:/checkhome";
    }
    
    @RequestMapping(path = "/user/updatefile", method = RequestMethod.POST)
    public String createOrUpdateEmployee(Demofile demofile,Model model) 
    {
    	ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
    
	   Demofile pdemofile  = demoFilesService.getDemofileOnly(demofile.getDemofilesid());
	
	   pdemofile.setStatus(demofile.getStatus()); 
	   pdemofile.setComments(demofile.getComments());
	   pdemofile.setModifiedby(user.getId()); 
	   pdemofile.setModifiedtime(new Date());
	  
	  
	    demoFilesService.updateStatusfile(pdemofile);
		  
	    return "redirect:/checkhome";
	       
    }


    @RequestMapping(value="/user/submitDemo", method = RequestMethod.GET)
    public ModelAndView submitDemo(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Demofile demo = new Demofile();
        modelAndView.addObject("demo", demo);
        String name=user.getName().toString();
       
        if(name.equals("admin")){
           
        }
        else {
            modelAndView.setViewName("user/dropDemo");
        }
        return modelAndView;
    }

    
    @RequestMapping(value="/user/profile", method = RequestMethod.GET)
    public ModelAndView profile(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("firstname",  user.getName() );
        modelAndView.addObject("lastname",  user.getLastName() );
        modelAndView.addObject("email",  user.getEmail() );
        String name=user.getName().toString();
        if(name.equals("admin")){
           
        }
        else {
            modelAndView.setViewName("user/profile");
        }
        return modelAndView;
    }

    @PostMapping("/user/upload") 
    public String singleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("demodesc") String demodesc,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
        	
    	   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
           User user = userService.findUserByEmail(auth.getName());
       
            byte[] bytes = file.getBytes();
           
            Date dt=new Date();
            String randomUUIDString = "uuid"+dt.getTime();
            
            String path = UPLOADED_FOLDER + randomUUIDString;

            Path dirpath = Paths.get(UPLOADED_FOLDER + randomUUIDString);
            Files.createDirectories(dirpath);
            
            Path fpath = Paths.get(path+ "//"+file.getOriginalFilename());
            Files.write(fpath, bytes);
            
            Demofile demofile= new Demofile();
            demofile.setCreatedtime(new Date());
            demofile.setFilename(file.getOriginalFilename());
            demofile.setFilelocation(path+ "//"+file.getOriginalFilename());
            demofile.setCreatedby(user.getId());
            demofile.setStatus("Unapproved");
            demofile.setFiledesc(demodesc);
            demoFilesService.saveDemofile(demofile);

            redirectAttributes.addFlashAttribute("message", 
                        "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/checkhome";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }


    @RequestMapping(value="/files", method = RequestMethod.GET)
    public  FileVO files(){
    	
    	FileVO vo = new FileVO();
    	
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        
        
        List<Demofile> list = demoFilesService.getDemofileList();
        vo.setData(list);
    	
		return vo;	
    	
    }

}
