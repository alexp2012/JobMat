package ro.jobmat.web.rest;

import ro.jobmat.domain.ApplicationMessage;
import ro.jobmat.service.ApplicationMessageService;
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
 * REST controller for managing {@link ro.jobmat.domain.ApplicationMessage}.
 */
@RestController
@RequestMapping("/api")
public class ApplicationMessageResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationMessageResource.class);

    private static final String ENTITY_NAME = "applicationMessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationMessageService applicationMessageService;

    public ApplicationMessageResource(ApplicationMessageService applicationMessageService) {
        this.applicationMessageService = applicationMessageService;
    }

    /**
     * {@code POST  /application-messages} : Create a new applicationMessage.
     *
     * @param applicationMessage the applicationMessage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationMessage, or with status {@code 400 (Bad Request)} if the applicationMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/application-messages")
    public ResponseEntity<ApplicationMessage> createApplicationMessage(@Valid @RequestBody ApplicationMessage applicationMessage) throws URISyntaxException {
        log.debug("REST request to save ApplicationMessage : {}", applicationMessage);
        if (applicationMessage.getId() != null) {
            throw new BadRequestAlertException("A new applicationMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationMessage result = applicationMessageService.save(applicationMessage);
        return ResponseEntity.created(new URI("/api/application-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-messages} : Updates an existing applicationMessage.
     *
     * @param applicationMessage the applicationMessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationMessage,
     * or with status {@code 400 (Bad Request)} if the applicationMessage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationMessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/application-messages")
    public ResponseEntity<ApplicationMessage> updateApplicationMessage(@Valid @RequestBody ApplicationMessage applicationMessage) throws URISyntaxException {
        log.debug("REST request to update ApplicationMessage : {}", applicationMessage);
        if (applicationMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApplicationMessage result = applicationMessageService.save(applicationMessage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationMessage.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /application-messages} : get all the applicationMessages.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationMessages in body.
     */
    @GetMapping("/application-messages")
    public ResponseEntity<List<ApplicationMessage>> getAllApplicationMessages(Pageable pageable) {
        log.debug("REST request to get a page of ApplicationMessages");
        Page<ApplicationMessage> page = applicationMessageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /application-messages/:id} : get the "id" applicationMessage.
     *
     * @param id the id of the applicationMessage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationMessage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-messages/{id}")
    public ResponseEntity<ApplicationMessage> getApplicationMessage(@PathVariable Long id) {
        log.debug("REST request to get ApplicationMessage : {}", id);
        Optional<ApplicationMessage> applicationMessage = applicationMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationMessage);
    }

    /**
     * {@code DELETE  /application-messages/:id} : delete the "id" applicationMessage.
     *
     * @param id the id of the applicationMessage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/application-messages/{id}")
    public ResponseEntity<Void> deleteApplicationMessage(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationMessage : {}", id);
        applicationMessageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
