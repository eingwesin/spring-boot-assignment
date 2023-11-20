package sg.gov.org.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.gov.org.model.SessionEntity;


@Repository("sessionRepository")
public interface SessionRepository extends JpaRepository<SessionEntity, String> {

	 SessionEntity findByEventNameIgnoreCase(String eventName);
}