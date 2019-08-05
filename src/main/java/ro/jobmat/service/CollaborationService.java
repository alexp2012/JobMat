package ro.jobmat.service;

import ro.jobmat.domain.Collaboration;
import ro.jobmat.repository.CollaborationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Collaboration}.
 */
@Service
@Transactional
public class CollaborationService {

    private final Logger log = LoggerFactory.getLogger(CollaborationService.class);

    private final CollaborationRepository collaborationRepository;

    public CollaborationService(CollaborationRepository collaborationRepository) {
        this.collaborationRepository = collaborationRepository;
    }

    /**
     * Save a collaboration.
     *
     * @param collaboration the entity to save.
     * @return the persisted entity.
     */
    public Collaboration save(Collaboration collaboration) {
        log.debug("Request to save Collaboration : {}", collaboration);
        return collaborationRepository.save(collaboration);
    }

    /**
     * Get all the collaborations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Collaboration> findAll(Pageable pageable) {
        log.debug("Request to get all Collaborations");
        return collaborationRepository.findAll(pageable);
    }


    /**
     * Get one collaboration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Collaboration> findOne(Long id) {
        log.debug("Request to get Collaboration : {}", id);
        return collaborationRepository.findById(id);
    }

    /**
     * Delete the collaboration by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Collaboration : {}", id);
        collaborationRepository.deleteById(id);
    }
}
