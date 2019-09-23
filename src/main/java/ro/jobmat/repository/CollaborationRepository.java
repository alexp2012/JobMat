package ro.jobmat.repository;
import ro.jobmat.domain.Collaboration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Collaboration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollaborationRepository extends JpaRepository<Collaboration, Long> {

}
