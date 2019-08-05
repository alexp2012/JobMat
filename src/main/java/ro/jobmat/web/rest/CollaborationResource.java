package ro.jobmat.web.rest;

import ro.jobmat.domain.Collaboration;
import ro.jobmat.service.CollaborationService;
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
 * REST controller for managing {@link ro.jobmat.domain.Collaboration}.
 */
@RestController
@RequestMapping("/api")
public class CollaborationResource {

    private final Logger log = LoggerFactory.getLogger(CollaborationResource.class);

    private static final String ENTITY_NAME = "collaboration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollaborationService collaborationService;

    public CollaborationResource(CollaborationService collaborationService) {
        this.collaborationService = collaborationService;
    }

    /**
     * {@code POST  /collaborations} : Create a new collaboration.
     *
     * @param collaboration the collaboration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collaboration, or with status {@code 400 (Bad Request)} if the collaboration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collaborations")
    public ResponseEntity<Collaboration> createCollaboration(@Valid @RequestBody Collaboration collaboration) throws URISyntaxException {
        log.debug("REST request to save Collaboration : {}", collaboration);
        if (collaboration.getId() != null) {
            throw new BadRequestAlertException("A new collaboration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Collaboration result = collaborationService.save(collaboration);
        return ResponseEntity.created(new URI("/api/collaborations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collaborations} : Updates an existing collaboration.
     *
     * @param collaboration the collaboration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collaboration,
     * or with status {@code 400 (Bad Request)} if the collaboration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collaboration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collaborations")
    public ResponseEntity<Collaboration> updateCollaboration(@Valid @RequestBody Collaboration collaboration) throws URISyntaxException {
        log.debug("REST request to update Collaboration : {}", collaboration);
        if (collaboration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Collaboration result = collaborationService.save(collaboration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collaboration.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /collaborations} : get all the collaborations.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collaborations in body.
     */
    @GetMapping("/collaborations")
    public ResponseEntity<List<Collaboration>> getAllCollaborations(Pageable pageable) {
        log.debug("REST request to get a page of Collaborations");
        Page<Collaboration> page = collaborationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /collaborations/:id} : get the "id" collaboration.
     *
     * @param id the id of the collaboration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collaboration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collaborations/{id}")
    public ResponseEntity<Collaboration> getCollaboration(@PathVariable Long id) {
        log.debug("REST request to get Collaboration : {}", id);
        Optional<Collaboration> collaboration = collaborationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collaboration);
    }

    /**
     * {@code DELETE  /collaborations/:id} : delete the "id" collaboration.
     *
     * @param id the id of the collaboration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collaborations/{id}")
    public ResponseEntity<Void> deleteCollaboration(@PathVariable Long id) {
        log.debug("REST request to delete Collaboration : {}", id);
        collaborationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
