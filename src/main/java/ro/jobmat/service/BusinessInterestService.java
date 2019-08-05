package ro.jobmat.service;

import ro.jobmat.domain.BusinessInterest;
import ro.jobmat.repository.BusinessInterestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BusinessInterest}.
 */
@Service
@Transactional
public class BusinessInterestService {

    private final Logger log = LoggerFactory.getLogger(BusinessInterestService.class);

    private final BusinessInterestRepository businessInterestRepository;

    public BusinessInterestService(BusinessInterestRepository businessInterestRepository) {
        this.businessInterestRepository = businessInterestRepository;
    }

    /**
     * Save a businessInterest.
     *
     * @param businessInterest the entity to save.
     * @return the persisted entity.
     */
    public BusinessInterest save(BusinessInterest businessInterest) {
        log.debug("Request to save BusinessInterest : {}", businessInterest);
        return businessInterestRepository.save(businessInterest);
    }

    /**
     * Get all the businessInterests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessInterest> findAll(Pageable pageable) {
        log.debug("Request to get all BusinessInterests");
        return businessInterestRepository.findAll(pageable);
    }


    /**
     * Get one businessInterest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BusinessInterest> findOne(Long id) {
        log.debug("Request to get BusinessInterest : {}", id);
        return businessInterestRepository.findById(id);
    }

    /**
     * Delete the businessInterest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BusinessInterest : {}", id);
        businessInterestRepository.deleteById(id);
    }
}
