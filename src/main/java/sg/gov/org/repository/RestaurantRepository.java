package sg.gov.org.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import sg.gov.org.model.RestaurantEntity;

@Repository("restaurantRepository")
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, String> {

	@Modifying
	@Transactional
	@Query(value="UPDATE restaurant SET votingcount = votingcount + 1 WHERE restaurant_id=:id",nativeQuery=true)
	 int updateVotingCount(@Param("id") String id);
	

}
