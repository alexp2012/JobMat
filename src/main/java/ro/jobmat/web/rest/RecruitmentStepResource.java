package ro.jobmat.web.rest;

import ro.jobmat.domain.RecruitmentStep;
import ro.jobmat.service.RecruitmentStepService;
import ro.jobmat.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ro.jobmat.domain.RecruitmentStep}.
 */
@RestController
@RequestMapping("/api")
public class RecruitmentStepResource {

    private final Logger log = LoggerFactory.getLogger(RecruitmentStepResource.class);

    private static final String ENTITY_NAME = "recruitmentStep";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecruitmentStepService recruitmentStepService;

    public RecruitmentStepResource(RecruitmentStepService recruitmentStepService) {
        this.recruitmentStepService = recruitmentStepService;
    }

    /**
     * {@code POST  /recruitment-steps} : Create a new recruitmentStep.
     *
     * @param recruitmentStep the recruitmentStep to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recruitmentStep, or with status {@code 400 (Bad Request)} if the recruitmentStep has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recruitment-steps")
    public ResponseEntity<RecruitmentStep> createRecruitmentStep(@Valid @RequestBody RecruitmentStep recruitmentStep) throws URISyntaxException {
        log.debug("REST request to save RecruitmentStep : {}", recruitmentStep);
        if (recruitmentStep.getId() != null) {
            throw new BadRequestAlertException("A new recruitmentStep cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecruitmentStep result = recruitmentStepService.save(recruitmentStep);
        return ResponseEntity.created(new URI("/api/recruitment-steps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recruitment-steps} : Updates an existing recruitmentStep.
     *
     * @param recruitmentStep the recruitmentStep to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruitmentStep,
     * or with status {@code 400 (Bad Request)} if the recruitmentStep is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recruitmentStep couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recruitment-steps")
    public ResponseEntity<RecruitmentStep> updateRecruitmentStep(@Valid @RequestBody RecruitmentStep recruitmentStep) throws URISyntaxException {
        log.debug("REST request to update RecruitmentStep : {}", recruitmentStep);
        if (recruitmentStep.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RecruitmentStep result = recruitmentStepService.save(recruitmentStep);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recruitmentStep.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /recruitment-steps} : get all the recruitmentSteps.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recruitmentSteps in body.
     */
    @GetMapping("/recruitment-steps")
    public ResponseEntity<List<RecruitmentStep>> getAllRecruitmentSteps(Pageable pageable) {
        log.debug("REST request to get a page of RecruitmentSteps");
        Page<RecruitmentStep> page = recruitmentStepService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /recruitment-steps/:id} : get the "id" recruitmentStep.
     *
     * @param id the id of the recruitmentStep to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recruitmentStep, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recruitment-steps/{id}")
    public ResponseEntity<RecruitmentStep> getRecruitmentStep(@PathVariable Long id) {
        log.debug("REST request to get RecruitmentStep : {}", id);
        Optional<RecruitmentStep> recruitmentStep = recruitmentStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(recruitmentStep);
    }

    /**
     * {@code DELETE  /recruitment-steps/:id} : delete the "id" recruitmentStep.
     *
     * @param id the id of the recruitmentStep to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recruitment-steps/{id}")
    public ResponseEntity<Void> deleteRecruitmentStep(@PathVariable Long id) {
        log.debug("REST request to delete RecruitmentStep : {}", id);
        recruitmentStepService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
