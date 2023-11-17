package sg.gov.org.dto;

import java.util.List;

import sg.gov.org.model.RestaurantEntity;

public class RestaurantDTO {
	 private List<RestaurantEntity> restaurants;

	

	public void addRestaurants(RestaurantEntity restaurant) {
        this.restaurants.add(restaurant);
    }



	public List<RestaurantEntity> getRestaurants() {
		return restaurants;
	}



	public void setRestaurants(List<RestaurantEntity> restaurants) {
		this.restaurants = restaurants;
	}
	    
}
