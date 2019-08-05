package ro.jobmat.service;

import ro.jobmat.domain.ApplicationMessage;
import ro.jobmat.repository.ApplicationMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ApplicationMessage}.
 */
@Service
@Transactional
public class ApplicationMessageService {

    private final Logger log = LoggerFactory.getLogger(ApplicationMessageService.class);

    private final ApplicationMessageRepository applicationMessageRepository;

    public ApplicationMessageService(ApplicationMessageRepository applicationMessageRepository) {
        this.applicationMessageRepository = applicationMessageRepository;
    }

    /**
     * Save a applicationMessage.
     *
     * @param applicationMessage the entity to save.
     * @return the persisted entity.
     */
    public ApplicationMessage save(ApplicationMessage applicationMessage) {
        log.debug("Request to save ApplicationMessage : {}", applicationMessage);
        return applicationMessageRepository.save(applicationMessage);
    }

    /**
     * Get all the applicationMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ApplicationMessage> findAll(Pageable pageable) {
        log.debug("Request to get all ApplicationMessages");
        return applicationMessageRepository.findAll(pageable);
    }


    /**
     * Get one applicationMessage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApplicationMessage> findOne(Long id) {
        log.debug("Request to get ApplicationMessage : {}", id);
        return applicationMessageRepository.findById(id);
    }

    /**
     * Delete the applicationMessage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ApplicationMessage : {}", id);
        applicationMessageRepository.deleteById(id);
    }
}
