package sg.gov.org.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sg.gov.org.model.UserEntity;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<UserEntity, String> {

    UserEntity findByEmailIdIgnoreCase(String emailId);

}
