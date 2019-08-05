package ro.jobmat.repository;

import ro.jobmat.domain.Opening;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Opening entity.
 */
@Repository
public interface OpeningRepository extends JpaRepository<Opening, Long> {

    @Query(value = "select distinct opening from Opening opening left join fetch opening.tags",
        countQuery = "select count(distinct opening) from Opening opening")
    Page<Opening> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct opening from Opening opening left join fetch opening.tags")
    List<Opening> findAllWithEagerRelationships();

    @Query("select opening from Opening opening left join fetch opening.tags where opening.id =:id")
    Optional<Opening> findOneWithEagerRelationships(@Param("id") Long id);

}
