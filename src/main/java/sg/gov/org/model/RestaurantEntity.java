package sg.gov.org.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "restaurant")
public class RestaurantEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="restaurant_id")
    private long restaurantID;

    @Column(name="name")
    private String name;

    @Column(name="location")
    private String location;
    
    @Column(name="type")
    private String type;
    
    @Column(name="price_level")
    private String priceLevel;
    
    @Column(name="public_review")
    private String publicReview;
    
    @Column(name="votingcount")
    private long votingCount;

	
	public long getVotingCount() {
		return votingCount;
	}

	public void setVotingCount(long votingCount) {
		this.votingCount = votingCount;
	}

	public long getRestaurantID() {
		return restaurantID;
	}

	public void setRestaurantID(long restaurantID) {
		this.restaurantID = restaurantID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPriceLevel() {
		return priceLevel;
	}

	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}

	public String getPublicReview() {
		return publicReview;
	}

	public void setPublicReview(String publicReview) {
		this.publicReview = publicReview;
	}
}
