package sg.gov.org.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sg.gov.org.ConfirmationToken;
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
	 
	 @PostMapping("/save")
	 public String save(ModelAndView modelAndView,RestaurantEntity entity) {
		 
		 System.out.println(entity.getName());
		
		// restaurantRepository.updateVotingCount(Id);
		 return "voting";
	 }
	 
//	 List<RestaurantEntity> restaurants=restaurantRepository.findAll();
//     
//     modelAndView.addObject("data",restaurants);
}
