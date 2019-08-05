package ro.jobmat.web.rest;

import ro.jobmat.JobMatApp;
import ro.jobmat.domain.Collaboration;
import ro.jobmat.domain.Company;
import ro.jobmat.repository.CollaborationRepository;
import ro.jobmat.service.CollaborationService;
import ro.jobmat.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static ro.jobmat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ro.jobmat.domain.enumeration.CollaborationStatus;
import ro.jobmat.domain.enumeration.CompanyType;
/**
 * Integration tests for the {@link CollaborationResource} REST controller.
 */
@SpringBootTest(classes = JobMatApp.class)
public class CollaborationResourceIT {

    private static final CollaborationStatus DEFAULT_STATUS = CollaborationStatus.ACTIVE;
    private static final CollaborationStatus UPDATED_STATUS = CollaborationStatus.INVITATION;

    private static final CompanyType DEFAULT_INITIATOR = CompanyType.SUPPLIER;
    private static final CompanyType UPDATED_INITIATOR = CompanyType.CUSTOMER;

    private static final Integer DEFAULT_INVITATIONS_NO = 1;
    private static final Integer UPDATED_INVITATIONS_NO = 2;
    private static final Integer SMALLER_INVITATIONS_NO = 1 - 1;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBBBBBBBBBBBB";

    private static final byte[] DEFAULT_CONTRACT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTRACT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTRACT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTRACT_CONTENT_TYPE = "image/png";

    @Autowired
    private CollaborationRepository collaborationRepository;

    @Autowired
    private CollaborationService collaborationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCollaborationMockMvc;

    private Collaboration collaboration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CollaborationResource collaborationResource = new CollaborationResource(collaborationService);
        this.restCollaborationMockMvc = MockMvcBuilders.standaloneSetup(collaborationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collaboration createEntity(EntityManager em) {
        Collaboration collaboration = new Collaboration()
            .status(DEFAULT_STATUS)
            .initiator(DEFAULT_INITIATOR)
            .invitationsNo(DEFAULT_INVITATIONS_NO)
            .message(DEFAULT_MESSAGE)
            .contract(DEFAULT_CONTRACT)
            .contractContentType(DEFAULT_CONTRACT_CONTENT_TYPE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        collaboration.setSupplier(company);
        // Add required entity
        collaboration.setCustomer(company);
        return collaboration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Collaboration createUpdatedEntity(EntityManager em) {
        Collaboration collaboration = new Collaboration()
            .status(UPDATED_STATUS)
            .initiator(UPDATED_INITIATOR)
            .invitationsNo(UPDATED_INVITATIONS_NO)
            .message(UPDATED_MESSAGE)
            .contract(UPDATED_CONTRACT)
            .contractContentType(UPDATED_CONTRACT_CONTENT_TYPE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        collaboration.setSupplier(company);
        // Add required entity
        collaboration.setCustomer(company);
        return collaboration;
    }

    @BeforeEach
    public void initTest() {
        collaboration = createEntity(em);
    }

    @Test
    @Transactional
    public void createCollaboration() throws Exception {
        int databaseSizeBeforeCreate = collaborationRepository.findAll().size();

        // Create the Collaboration
        restCollaborationMockMvc.perform(post("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboration)))
            .andExpect(status().isCreated());

        // Validate the Collaboration in the database
        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeCreate + 1);
        Collaboration testCollaboration = collaborationList.get(collaborationList.size() - 1);
        assertThat(testCollaboration.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCollaboration.getInitiator()).isEqualTo(DEFAULT_INITIATOR);
        assertThat(testCollaboration.getInvitationsNo()).isEqualTo(DEFAULT_INVITATIONS_NO);
        assertThat(testCollaboration.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testCollaboration.getContract()).isEqualTo(DEFAULT_CONTRACT);
        assertThat(testCollaboration.getContractContentType()).isEqualTo(DEFAULT_CONTRACT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createCollaborationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = collaborationRepository.findAll().size();

        // Create the Collaboration with an existing ID
        collaboration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollaborationMockMvc.perform(post("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboration)))
            .andExpect(status().isBadRequest());

        // Validate the Collaboration in the database
        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaborationRepository.findAll().size();
        // set the field null
        collaboration.setStatus(null);

        // Create the Collaboration, which fails.

        restCollaborationMockMvc.perform(post("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboration)))
            .andExpect(status().isBadRequest());

        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInitiatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaborationRepository.findAll().size();
        // set the field null
        collaboration.setInitiator(null);

        // Create the Collaboration, which fails.

        restCollaborationMockMvc.perform(post("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboration)))
            .andExpect(status().isBadRequest());

        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInvitationsNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaborationRepository.findAll().size();
        // set the field null
        collaboration.setInvitationsNo(null);

        // Create the Collaboration, which fails.

        restCollaborationMockMvc.perform(post("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboration)))
            .andExpect(status().isBadRequest());

        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaborationRepository.findAll().size();
        // set the field null
        collaboration.setMessage(null);

        // Create the Collaboration, which fails.

        restCollaborationMockMvc.perform(post("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboration)))
            .andExpect(status().isBadRequest());

        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCollaborations() throws Exception {
        // Initialize the database
        collaborationRepository.saveAndFlush(collaboration);

        // Get all the collaborationList
        restCollaborationMockMvc.perform(get("/api/collaborations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collaboration.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].initiator").value(hasItem(DEFAULT_INITIATOR.toString())))
            .andExpect(jsonPath("$.[*].invitationsNo").value(hasItem(DEFAULT_INVITATIONS_NO)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].contractContentType").value(hasItem(DEFAULT_CONTRACT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contract").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTRACT))));
    }
    
    @Test
    @Transactional
    public void getCollaboration() throws Exception {
        // Initialize the database
        collaborationRepository.saveAndFlush(collaboration);

        // Get the collaboration
        restCollaborationMockMvc.perform(get("/api/collaborations/{id}", collaboration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(collaboration.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.initiator").value(DEFAULT_INITIATOR.toString()))
            .andExpect(jsonPath("$.invitationsNo").value(DEFAULT_INVITATIONS_NO))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.contractContentType").value(DEFAULT_CONTRACT_CONTENT_TYPE))
            .andExpect(jsonPath("$.contract").value(Base64Utils.encodeToString(DEFAULT_CONTRACT)));
    }

    @Test
    @Transactional
    public void getNonExistingCollaboration() throws Exception {
        // Get the collaboration
        restCollaborationMockMvc.perform(get("/api/collaborations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollaboration() throws Exception {
        // Initialize the database
        collaborationService.save(collaboration);

        int databaseSizeBeforeUpdate = collaborationRepository.findAll().size();

        // Update the collaboration
        Collaboration updatedCollaboration = collaborationRepository.findById(collaboration.getId()).get();
        // Disconnect from session so that the updates on updatedCollaboration are not directly saved in db
        em.detach(updatedCollaboration);
        updatedCollaboration
            .status(UPDATED_STATUS)
            .initiator(UPDATED_INITIATOR)
            .invitationsNo(UPDATED_INVITATIONS_NO)
            .message(UPDATED_MESSAGE)
            .contract(UPDATED_CONTRACT)
            .contractContentType(UPDATED_CONTRACT_CONTENT_TYPE);

        restCollaborationMockMvc.perform(put("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCollaboration)))
            .andExpect(status().isOk());

        // Validate the Collaboration in the database
        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeUpdate);
        Collaboration testCollaboration = collaborationList.get(collaborationList.size() - 1);
        assertThat(testCollaboration.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCollaboration.getInitiator()).isEqualTo(UPDATED_INITIATOR);
        assertThat(testCollaboration.getInvitationsNo()).isEqualTo(UPDATED_INVITATIONS_NO);
        assertThat(testCollaboration.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testCollaboration.getContract()).isEqualTo(UPDATED_CONTRACT);
        assertThat(testCollaboration.getContractContentType()).isEqualTo(UPDATED_CONTRACT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCollaboration() throws Exception {
        int databaseSizeBeforeUpdate = collaborationRepository.findAll().size();

        // Create the Collaboration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollaborationMockMvc.perform(put("/api/collaborations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(collaboration)))
            .andExpect(status().isBadRequest());

        // Validate the Collaboration in the database
        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCollaboration() throws Exception {
        // Initialize the database
        collaborationService.save(collaboration);

        int databaseSizeBeforeDelete = collaborationRepository.findAll().size();

        // Delete the collaboration
        restCollaborationMockMvc.perform(delete("/api/collaborations/{id}", collaboration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Collaboration> collaborationList = collaborationRepository.findAll();
        assertThat(collaborationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Collaboration.class);
        Collaboration collaboration1 = new Collaboration();
        collaboration1.setId(1L);
        Collaboration collaboration2 = new Collaboration();
        collaboration2.setId(collaboration1.getId());
        assertThat(collaboration1).isEqualTo(collaboration2);
        collaboration2.setId(2L);
        assertThat(collaboration1).isNotEqualTo(collaboration2);
        collaboration1.setId(null);
        assertThat(collaboration1).isNotEqualTo(collaboration2);
    }
}
