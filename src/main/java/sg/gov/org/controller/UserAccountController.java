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
import sg.gov.org.model.UserEntity;
import sg.gov.org.repository.ConfirmationTokenRepository;
import sg.gov.org.repository.RestaurantRepository;
import sg.gov.org.repository.UserRepository;
import sg.gov.org.service.EmailService;

@Controller
public class UserAccountController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView displayRegistration(ModelAndView modelAndView, UserEntity userEntity)
    {
        modelAndView.addObject("userEntity", userEntity);
        modelAndView.setViewName("register");
        return modelAndView;
    }
    @RequestMapping(value="/navbar", method = RequestMethod.GET)
    public ModelAndView displayNavBar(ModelAndView modelAndView, UserEntity userEntity)
    {
        
        modelAndView.setViewName("navbar");
        return modelAndView;
    }
    
    
    
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ModelAndView registerUser(ModelAndView modelAndView, UserEntity userEntity)
    {

    	UserEntity existingUser = userRepository.findByEmailIdIgnoreCase(userEntity.getEmailId());
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
        	UserEntity user = userRepository.findByEmailIdIgnoreCase(token.getUserEntity().getEmailId());
            user.setEnabled(true);
            userRepository.save(user);
            
            
           
            modelAndView.setViewName("accountVerified");
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
