package ro.jobmat.service;

import ro.jobmat.domain.Opening;
import ro.jobmat.repository.OpeningRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Opening}.
 */
@Service
@Transactional
public class OpeningService {

    private final Logger log = LoggerFactory.getLogger(OpeningService.class);

    private final OpeningRepository openingRepository;

    public OpeningService(OpeningRepository openingRepository) {
        this.openingRepository = openingRepository;
    }

    /**
     * Save a opening.
     *
     * @param opening the entity to save.
     * @return the persisted entity.
     */
    public Opening save(Opening opening) {
        log.debug("Request to save Opening : {}", opening);
        return openingRepository.save(opening);
    }

    /**
     * Get all the openings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Opening> findAll(Pageable pageable) {
        log.debug("Request to get all Openings");
        return openingRepository.findAll(pageable);
    }

    /**
     * Get all the openings with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Opening> findAllWithEagerRelationships(Pageable pageable) {
        return openingRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one opening by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Opening> findOne(Long id) {
        log.debug("Request to get Opening : {}", id);
        return openingRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the opening by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Opening : {}", id);
        openingRepository.deleteById(id);
    }
}
