package ro.jobmat.repository;
import ro.jobmat.domain.BusinessInterest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BusinessInterest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessInterestRepository extends JpaRepository<BusinessInterest, Long> {

}
