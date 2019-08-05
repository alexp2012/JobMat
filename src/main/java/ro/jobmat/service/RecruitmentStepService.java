package ro.jobmat.service;

import ro.jobmat.domain.RecruitmentStep;
import ro.jobmat.repository.RecruitmentStepRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RecruitmentStep}.
 */
@Service
@Transactional
public class RecruitmentStepService {

    private final Logger log = LoggerFactory.getLogger(RecruitmentStepService.class);

    private final RecruitmentStepRepository recruitmentStepRepository;

    public RecruitmentStepService(RecruitmentStepRepository recruitmentStepRepository) {
        this.recruitmentStepRepository = recruitmentStepRepository;
    }

    /**
     * Save a recruitmentStep.
     *
     * @param recruitmentStep the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentStep save(RecruitmentStep recruitmentStep) {
        log.debug("Request to save RecruitmentStep : {}", recruitmentStep);
        return recruitmentStepRepository.save(recruitmentStep);
    }

    /**
     * Get all the recruitmentSteps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RecruitmentStep> findAll(Pageable pageable) {
        log.debug("Request to get all RecruitmentSteps");
        return recruitmentStepRepository.findAll(pageable);
    }


    /**
     * Get one recruitmentStep by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecruitmentStep> findOne(Long id) {
        log.debug("Request to get RecruitmentStep : {}", id);
        return recruitmentStepRepository.findById(id);
    }

    /**
     * Delete the recruitmentStep by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RecruitmentStep : {}", id);
        recruitmentStepRepository.deleteById(id);
    }
}
