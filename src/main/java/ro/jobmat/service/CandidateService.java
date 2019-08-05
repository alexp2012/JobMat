package ro.jobmat.service;

import ro.jobmat.domain.Candidate;
import ro.jobmat.repository.CandidateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Candidate}.
 */
@Service
@Transactional
public class CandidateService {

    private final Logger log = LoggerFactory.getLogger(CandidateService.class);

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    /**
     * Save a candidate.
     *
     * @param candidate the entity to save.
     * @return the persisted entity.
     */
    public Candidate save(Candidate candidate) {
        log.debug("Request to save Candidate : {}", candidate);
        return candidateRepository.save(candidate);
    }

    /**
     * Get all the candidates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Candidate> findAll(Pageable pageable) {
        log.debug("Request to get all Candidates");
        return candidateRepository.findAll(pageable);
    }


    /**
     * Get one candidate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Candidate> findOne(Long id) {
        log.debug("Request to get Candidate : {}", id);
        return candidateRepository.findById(id);
    }

    /**
     * Delete the candidate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Candidate : {}", id);
        candidateRepository.deleteById(id);
    }
}
