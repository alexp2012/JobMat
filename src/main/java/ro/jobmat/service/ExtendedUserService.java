package ro.jobmat.service;

import ro.jobmat.domain.ExtendedUser;
import ro.jobmat.repository.ExtendedUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ExtendedUser}.
 */
@Service
@Transactional
public class ExtendedUserService {

    private final Logger log = LoggerFactory.getLogger(ExtendedUserService.class);

    private final ExtendedUserRepository extendedUserRepository;

    public ExtendedUserService(ExtendedUserRepository extendedUserRepository) {
        this.extendedUserRepository = extendedUserRepository;
    }

    /**
     * Save a extendedUser.
     *
     * @param extendedUser the entity to save.
     * @return the persisted entity.
     */
    public ExtendedUser save(ExtendedUser extendedUser) {
        log.debug("Request to save ExtendedUser : {}", extendedUser);
        return extendedUserRepository.save(extendedUser);
    }

    /**
     * Get all the extendedUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ExtendedUser> findAll(Pageable pageable) {
        log.debug("Request to get all ExtendedUsers");
        return extendedUserRepository.findAll(pageable);
    }


    /**
     * Get one extendedUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ExtendedUser> findOne(Long id) {
        log.debug("Request to get ExtendedUser : {}", id);
        return extendedUserRepository.findById(id);
    }

    /**
     * Delete the extendedUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ExtendedUser : {}", id);
        extendedUserRepository.deleteById(id);
    }
}
