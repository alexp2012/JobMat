package ro.jobmat.repository;

import ro.jobmat.domain.RecruitmentStep;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RecruitmentStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecruitmentStepRepository extends JpaRepository<RecruitmentStep, Long> {

}
