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

@Controller
public class RestaurantsVotingController {
	 @Autowired
	    private RestaurantRepository restaurantRepository;
	 
	 
	 @RequestMapping(value="/voting", method = RequestMethod.GET)
	    public ModelAndView displayRegistration(ModelAndView modelAndView, UserEntity userEntity)
	    {
	        modelAndView.addObject("userEntity", userEntity);
	        modelAndView.setViewName("voting");
	        return modelAndView;
	    }
	 
	 
	 
	 @GetMapping("/all")
	 public String showAll(Model model) {
	     model.addAttribute("list", restaurantRepository.findAll());
	     return "voting";
	 }
	 
	 
	 @GetMapping("/result")
	 public String showResult(Model model) {
	     model.addAttribute("list", restaurantRepository.findAll());
	     return "result";
	 }
	 
//	 @PostMapping("/save")
//	 public String save(ModelAndView modelAndView,RestaurantEntity entity) {
//		 
//		 System.out.println(entity.getName());
//		
//		// restaurantRepository.updateVotingCount(Id);
//		 return "voting";
//	 }
//	 
	 
//	 @PostMapping("/save")
//	 public String saveBooks(@ModelAttribute RestaurantDTO form, Model model) {
//		 
//		 if(form!=null) {
//			 List<RestaurantEntity> restaurants=form.getRestaurants();
//			 
//			 for(RestaurantEntity restaruant:restaurants) {
//				 System.out.println("this is "+ restaruant.getName());
//			 }
//		     
//			 //bookService.saveAll(form.getRestaurants());
//
//		   //  model.addAttribute("books", bookService.findAll());
//		     
//			 
//		 }
//		 return "save";
//	 }
	 
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
	        // Logic to check if there are updates in the database
	        // Return true if there are updates, false otherwise
		  
		  //HashMap<String,Object> map=new HashMap<String,Object>();
		 // boolean checking=true;
		 // map.put("message",checking);
		  //model.addAttribute("list", restaurantRepository.findAll()); 

	     // return new ResponseEntity<HashMap<String,Object>>(map, HttpStatus.OK); 
		  
		  List<RestaurantEntity> list=restaurantRepository.findAll();
		  return new ResponseEntity<List<RestaurantEntity>>(list,HttpStatus.OK);
	   
	  }
	  
//	 List<RestaurantEntity> restaurants=restaurantRepository.findAll();
//     
//     modelAndView.addObject("data",restaurants);
	  
	  
	  
	  @GetMapping("/refreshdata")
	  public ModelAndView getResultBySearchKey()
	      {
		  	List<RestaurantEntity> list=restaurantRepository.findAll();//results from db
	          ModelAndView mv= new ModelAndView("voting::search_list"); 
	          mv.addObject("list",list);

	          return mv;
	      }
}
