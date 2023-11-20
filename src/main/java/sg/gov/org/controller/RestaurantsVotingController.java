package sg.gov.org.controller;

import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLEngineResult.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sg.gov.org.ConfirmationToken;
import sg.gov.org.dto.RestaurantDTO;
import sg.gov.org.model.RestaurantEntity;
import sg.gov.org.model.UserEntity;
import sg.gov.org.repository.RestaurantRepository;
import sg.gov.org.repository.UserRepository;

@Controller
public class RestaurantsVotingController {
	 @Autowired
	  private RestaurantRepository restaurantRepository;
	 
	 @Autowired
	  private UserRepository userRepository;
	 
	 @RequestMapping(value="/result", method = RequestMethod.GET)
	 public  ModelAndView displayResult(ModelAndView modelAndView,UserEntity userEntity) {
		 modelAndView.addObject("list", restaurantRepository.findAll());
		 modelAndView.setViewName("result");
	     return modelAndView;
	 }
	 
	 
	 @RequestMapping(value="/generatedResult", method = RequestMethod.GET)
	 public  ModelAndView generateFinal(ModelAndView modelAndView,UserEntity userEntity) {
		 modelAndView.setViewName("/generatedResult");
	     return modelAndView;
	 }
	 
	 
	 @RequestMapping(value="/voting", method = RequestMethod.GET)
	 public ModelAndView displayVoting(ModelAndView modelAndView,UserEntity userEntity) {
		 
		 modelAndView.addObject("userEntity", userEntity);
		 modelAndView.addObject ("list", restaurantRepository.findAll());
		 modelAndView.setViewName("voting");
	     return modelAndView;
	 }
	 
	 

	 
	  @PostMapping("/save")
	 public String processSelected(@RequestParam("selectedItems") List<String> selectedItems,Model model) {
	        // Process the selected items in your backend
	        // ...
		  
		   for(String item: selectedItems) {
			   
			   restaurantRepository.updateVotingCount(item);
		   }
		   
		   
		   model.addAttribute("list", restaurantRepository.findAll());
	        return "redirect:/result";
	  }
	 
	  
	  @GetMapping("/check-updates")
	   public  ResponseEntity<List<RestaurantEntity>> checkUpdates(Model model){ 
	       
		  
		  List<RestaurantEntity> list=restaurantRepository.findAll();
		  return new ResponseEntity<List<RestaurantEntity>>(list,HttpStatus.OK);
	   
	  }

	  
	  @GetMapping("/refreshdata")
	  public ModelAndView getResultBySearchKey()
	      {
		  	List<RestaurantEntity> list=restaurantRepository.findAll();
	          ModelAndView mv= new ModelAndView("voting::search_list"); 
	          mv.addObject("list",list);

	          return mv;
	      }
}
