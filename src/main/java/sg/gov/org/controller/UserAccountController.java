package sg.gov.org.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sg.gov.org.ConfirmationToken;
import sg.gov.org.model.RestaurantEntity;
import sg.gov.org.model.SessionEntity;
import sg.gov.org.model.UserEntity;
import sg.gov.org.repository.ConfirmationTokenRepository;
import sg.gov.org.repository.RestaurantRepository;
import sg.gov.org.repository.SessionRepository;
import sg.gov.org.repository.UserRepository;
import sg.gov.org.service.EmailService;

@Controller
public class UserAccountController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private  SessionRepository sesRepository;
    
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView loginForm(ModelAndView modelAndView, UserEntity userEntity)
    {
        modelAndView.addObject("userEntity", userEntity);
        modelAndView.setViewName("login");
        return modelAndView;
    }
  
    
    @RequestMapping(value="/navbar", method = RequestMethod.GET)
    public ModelAndView displayNavBar(ModelAndView modelAndView, UserEntity userEntity)
    {
        
        modelAndView.setViewName("navbar");
        return modelAndView;
    }
    
    @RequestMapping(value="/createSession", method = RequestMethod.GET)
    public ModelAndView displaySession(ModelAndView modelAndView, SessionEntity sessEntity)
    {
    	 modelAndView.addObject("sessEntity", sessEntity);
        modelAndView.setViewName("eventCreate");
        return modelAndView;
    }
  
    @RequestMapping(value="/createSession/save", method = RequestMethod.POST)
    public ModelAndView createSession(ModelAndView modelAndView, SessionEntity sessEntity)
    
    {
    	
    	SessionEntity existingEvent =sesRepository.findByEventNameIgnoreCase(sessEntity.getEventName());
    	 if(existingEvent != null)
         {
             modelAndView.addObject("message","This event already exists!");
            // modelAndView.setViewName("error");
             modelAndView.setViewName("eventCreate");
            
         }else {
        	 sessEntity.setStatus("A");
         	sesRepository.save(sessEntity);
         	
         	modelAndView.addObject("message","Event has been created !!");
             modelAndView.setViewName("eventCreate");
           
         }
    		
    	  return modelAndView;
    	
    }
  
    @RequestMapping(value="/endSession", method = RequestMethod.GET)
    public ModelAndView displayEndSession(ModelAndView modelAndView, SessionEntity sessEntity)
    {
    	
        modelAndView.setViewName("eventStop");
        return modelAndView;
    }
    
    @RequestMapping(value="/endSession/save", method = RequestMethod.POST)
    public ModelAndView endSession(ModelAndView modelAndView, SessionEntity sessEntity)
    {
    	
    	SessionEntity existingEvent =sesRepository.findByEventNameIgnoreCase(sessEntity.getEventName());
   	  if(existingEvent != null)
        {
   		existingEvent.setStatus("D");
    	sesRepository.save(existingEvent);
    	
    	modelAndView.addObject("message","Event has been ended !!");
        modelAndView.setViewName("eventStop");
           
        }else {
        	modelAndView.addObject("message","This event is not available to stop!!");
            // modelAndView.setViewName("error");
             modelAndView.setViewName("eventStop");
        }
    	
    	
        return modelAndView;
    }
    
    @RequestMapping(value="/authorizedUser", method = RequestMethod.GET)
    public ModelAndView authorizedUser(ModelAndView modelAndView, UserEntity userEntity)
    {
    	
    	if(userEntity.getEmailId().equals("adminuser1@gmail.com") || userEntity.getEmailId().equals("adminuser2@gmail.com"))
    	{
    		modelAndView.setViewName("register");
    	}
       
        
        return modelAndView;
    }
    
    @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView displayRegistration(ModelAndView modelAndView, UserEntity userEntity)
    {
        modelAndView.addObject("userEntity", userEntity);
        modelAndView.setViewName("register");
        return modelAndView;
    }
   
    
   
 
    
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ModelAndView registerUser(ModelAndView modelAndView, UserEntity userEntity)
    {

    	UserEntity existingUser = userRepository.findByEmailIdAndEventName(userEntity.getEmailId(),userEntity.getEventName());
        if(existingUser != null)
        {
            modelAndView.addObject("message","This email already exists!");
           // modelAndView.setViewName("error");
            modelAndView.setViewName("register");
        }
        else
        {
            userRepository.save(userEntity);

            ConfirmationToken confirmationToken = new ConfirmationToken(userEntity);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userEntity.getEmailId());
            mailMessage.setSubject("Invitation to vote restaurant!");
            mailMessage.setText("To join restaurant voting system, please click here : "
            +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailService.sendEmail(mailMessage);

            modelAndView.addObject("emailId", userEntity.getEmailId()); 

            modelAndView.addObject("message","Email has been sent !!");
          //  modelAndView.setViewName("successfulRegisteration");
            modelAndView.setViewName("register");
        }

        return modelAndView;
    }
    

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
        	UserEntity user = userRepository.findByEmailIdAndEventName(token.getUserEntity().getEmailId(),token.getUserEntity().getEventName());
        	
        	if(user !=null ){
        	SessionEntity event=sesRepository.findByEventNameIgnoreCase(user.getEventName());
        	
        	if(event.getStatus().equals("D")) {
        		modelAndView.addObject("message","Event session has been expired!!");
                modelAndView.setViewName("accountVerified");
        		
        	}else {
        		user.setEnabled(true);
                userRepository.save(user);
                String message="Congratulations! Your account has been activated and email is verified!";
                modelAndView.addObject("message",message);
                modelAndView.setViewName("accountVerified");
        	}
        	}else {
        		 modelAndView.addObject("message","User don't have access to event session!");
                 modelAndView.setViewName("error");
        	}
        }
        else
        {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }
    
    /*
     * Below are API services to call from Angular
     * */
    
    
    @RequestMapping(value="/invite", method = RequestMethod.POST)	
	public ResponseEntity<HashMap<String,Object>> registerToInvite(@RequestBody String emailId) {
		
    	HashMap<String,Object> map=new HashMap<String,Object>();
    	
    	UserEntity existingUser = userRepository.findByEmailIdIgnoreCase(emailId);
        if(existingUser != null)
        {
            
            map.put("message","Duplicate Email");
            return new ResponseEntity<HashMap<String,Object>>(map, HttpStatus.BAD_REQUEST);
        }
        else
        {
        	UserEntity userEntity=new UserEntity();
        	userEntity.setEmailId(emailId);
            userRepository.save(userEntity);

            ConfirmationToken confirmationToken = new ConfirmationToken(userEntity);

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userEntity.getEmailId());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setText("To confirm your account, please click here : "
            +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailService.sendEmail(mailMessage);

            map.put("message","success");
            return new ResponseEntity<HashMap<String,Object>>(map, HttpStatus.OK);
        }
        
	}
	
 /*   @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView displayRegistration(ModelAndView modelAndView, UserEntity userEntity)
    {
        modelAndView.addObject("userEntity", userEntity);
        modelAndView.setViewName("register");
        return modelAndView;
    }*/
    
}
