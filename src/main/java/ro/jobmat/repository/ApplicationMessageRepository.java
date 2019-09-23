package ro.jobmat.repository;
import ro.jobmat.domain.ApplicationMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ApplicationMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationMessageRepository extends JpaRepository<ApplicationMessage, Long> {

}
