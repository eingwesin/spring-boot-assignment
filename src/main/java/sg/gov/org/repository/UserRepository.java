package sg.gov.org.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.gov.org.model.UserEntity;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<UserEntity, String> {

    UserEntity findByEmailIdIgnoreCase(String emailId);
    
    UserEntity findByEmailIdAndEventName(String emailId,String eventName);
    
   /* @Query(value="UPDATE restaurant SET votingcount = votingcount + 1 WHERE restaurant_id=:id",nativeQuery=true)
  	 int updateVotingCount(@Param("id") String id);*/

}
