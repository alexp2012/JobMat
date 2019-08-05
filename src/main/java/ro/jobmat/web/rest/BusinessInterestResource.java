package ro.jobmat.web.rest;

import ro.jobmat.domain.BusinessInterest;
import ro.jobmat.service.BusinessInterestService;
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
 * REST controller for managing {@link ro.jobmat.domain.BusinessInterest}.
 */
@RestController
@RequestMapping("/api")
public class BusinessInterestResource {

    private final Logger log = LoggerFactory.getLogger(BusinessInterestResource.class);

    private static final String ENTITY_NAME = "businessInterest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessInterestService businessInterestService;

    public BusinessInterestResource(BusinessInterestService businessInterestService) {
        this.businessInterestService = businessInterestService;
    }

    /**
     * {@code POST  /business-interests} : Create a new businessInterest.
     *
     * @param businessInterest the businessInterest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessInterest, or with status {@code 400 (Bad Request)} if the businessInterest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-interests")
    public ResponseEntity<BusinessInterest> createBusinessInterest(@Valid @RequestBody BusinessInterest businessInterest) throws URISyntaxException {
        log.debug("REST request to save BusinessInterest : {}", businessInterest);
        if (businessInterest.getId() != null) {
            throw new BadRequestAlertException("A new businessInterest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessInterest result = businessInterestService.save(businessInterest);
        return ResponseEntity.created(new URI("/api/business-interests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-interests} : Updates an existing businessInterest.
     *
     * @param businessInterest the businessInterest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessInterest,
     * or with status {@code 400 (Bad Request)} if the businessInterest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessInterest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-interests")
    public ResponseEntity<BusinessInterest> updateBusinessInterest(@Valid @RequestBody BusinessInterest businessInterest) throws URISyntaxException {
        log.debug("REST request to update BusinessInterest : {}", businessInterest);
        if (businessInterest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BusinessInterest result = businessInterestService.save(businessInterest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessInterest.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /business-interests} : get all the businessInterests.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessInterests in body.
     */
    @GetMapping("/business-interests")
    public ResponseEntity<List<BusinessInterest>> getAllBusinessInterests(Pageable pageable) {
        log.debug("REST request to get a page of BusinessInterests");
        Page<BusinessInterest> page = businessInterestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-interests/:id} : get the "id" businessInterest.
     *
     * @param id the id of the businessInterest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessInterest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-interests/{id}")
    public ResponseEntity<BusinessInterest> getBusinessInterest(@PathVariable Long id) {
        log.debug("REST request to get BusinessInterest : {}", id);
        Optional<BusinessInterest> businessInterest = businessInterestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessInterest);
    }

    /**
     * {@code DELETE  /business-interests/:id} : delete the "id" businessInterest.
     *
     * @param id the id of the businessInterest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-interests/{id}")
    public ResponseEntity<Void> deleteBusinessInterest(@PathVariable Long id) {
        log.debug("REST request to delete BusinessInterest : {}", id);
        businessInterestService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
