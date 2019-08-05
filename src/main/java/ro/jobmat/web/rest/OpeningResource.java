package ro.jobmat.web.rest;

import ro.jobmat.domain.Opening;
import ro.jobmat.service.OpeningService;
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
 * REST controller for managing {@link ro.jobmat.domain.Opening}.
 */
@RestController
@RequestMapping("/api")
public class OpeningResource {

    private final Logger log = LoggerFactory.getLogger(OpeningResource.class);

    private static final String ENTITY_NAME = "opening";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OpeningService openingService;

    public OpeningResource(OpeningService openingService) {
        this.openingService = openingService;
    }

    /**
     * {@code POST  /openings} : Create a new opening.
     *
     * @param opening the opening to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new opening, or with status {@code 400 (Bad Request)} if the opening has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/openings")
    public ResponseEntity<Opening> createOpening(@Valid @RequestBody Opening opening) throws URISyntaxException {
        log.debug("REST request to save Opening : {}", opening);
        if (opening.getId() != null) {
            throw new BadRequestAlertException("A new opening cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Opening result = openingService.save(opening);
        return ResponseEntity.created(new URI("/api/openings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /openings} : Updates an existing opening.
     *
     * @param opening the opening to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opening,
     * or with status {@code 400 (Bad Request)} if the opening is not valid,
     * or with status {@code 500 (Internal Server Error)} if the opening couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/openings")
    public ResponseEntity<Opening> updateOpening(@Valid @RequestBody Opening opening) throws URISyntaxException {
        log.debug("REST request to update Opening : {}", opening);
        if (opening.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Opening result = openingService.save(opening);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, opening.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /openings} : get all the openings.
     *

     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of openings in body.
     */
    @GetMapping("/openings")
    public ResponseEntity<List<Opening>> getAllOpenings(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Openings");
        Page<Opening> page;
        if (eagerload) {
            page = openingService.findAllWithEagerRelationships(pageable);
        } else {
            page = openingService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /openings/:id} : get the "id" opening.
     *
     * @param id the id of the opening to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the opening, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/openings/{id}")
    public ResponseEntity<Opening> getOpening(@PathVariable Long id) {
        log.debug("REST request to get Opening : {}", id);
        Optional<Opening> opening = openingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(opening);
    }

    /**
     * {@code DELETE  /openings/:id} : delete the "id" opening.
     *
     * @param id the id of the opening to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/openings/{id}")
    public ResponseEntity<Void> deleteOpening(@PathVariable Long id) {
        log.debug("REST request to delete Opening : {}", id);
        openingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
