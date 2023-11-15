package sg.gov.org.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sg.gov.org.ConfirmationToken;

@Repository("confirmationTokenRepository")
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
}

