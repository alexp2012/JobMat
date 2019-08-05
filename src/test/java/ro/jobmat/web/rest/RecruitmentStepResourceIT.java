package ro.jobmat.web.rest;

import ro.jobmat.JobMatApp;
import ro.jobmat.domain.RecruitmentStep;
import ro.jobmat.domain.Opening;
import ro.jobmat.repository.RecruitmentStepRepository;
import ro.jobmat.service.RecruitmentStepService;
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
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static ro.jobmat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RecruitmentStepResource} REST controller.
 */
@SpringBootTest(classes = JobMatApp.class)
public class RecruitmentStepResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEQUENCE = 1;
    private static final Integer UPDATED_SEQUENCE = 2;
    private static final Integer SMALLER_SEQUENCE = 1 - 1;

    @Autowired
    private RecruitmentStepRepository recruitmentStepRepository;

    @Autowired
    private RecruitmentStepService recruitmentStepService;

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

    private MockMvc restRecruitmentStepMockMvc;

    private RecruitmentStep recruitmentStep;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RecruitmentStepResource recruitmentStepResource = new RecruitmentStepResource(recruitmentStepService);
        this.restRecruitmentStepMockMvc = MockMvcBuilders.standaloneSetup(recruitmentStepResource)
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
    public static RecruitmentStep createEntity(EntityManager em) {
        RecruitmentStep recruitmentStep = new RecruitmentStep()
            .description(DEFAULT_DESCRIPTION)
            .sequence(DEFAULT_SEQUENCE);
        // Add required entity
        Opening opening;
        if (TestUtil.findAll(em, Opening.class).isEmpty()) {
            opening = OpeningResourceIT.createEntity(em);
            em.persist(opening);
            em.flush();
        } else {
            opening = TestUtil.findAll(em, Opening.class).get(0);
        }
        recruitmentStep.setOpening(opening);
        return recruitmentStep;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecruitmentStep createUpdatedEntity(EntityManager em) {
        RecruitmentStep recruitmentStep = new RecruitmentStep()
            .description(UPDATED_DESCRIPTION)
            .sequence(UPDATED_SEQUENCE);
        // Add required entity
        Opening opening;
        if (TestUtil.findAll(em, Opening.class).isEmpty()) {
            opening = OpeningResourceIT.createUpdatedEntity(em);
            em.persist(opening);
            em.flush();
        } else {
            opening = TestUtil.findAll(em, Opening.class).get(0);
        }
        recruitmentStep.setOpening(opening);
        return recruitmentStep;
    }

    @BeforeEach
    public void initTest() {
        recruitmentStep = createEntity(em);
    }

    @Test
    @Transactional
    public void createRecruitmentStep() throws Exception {
        int databaseSizeBeforeCreate = recruitmentStepRepository.findAll().size();

        // Create the RecruitmentStep
        restRecruitmentStepMockMvc.perform(post("/api/recruitment-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruitmentStep)))
            .andExpect(status().isCreated());

        // Validate the RecruitmentStep in the database
        List<RecruitmentStep> recruitmentStepList = recruitmentStepRepository.findAll();
        assertThat(recruitmentStepList).hasSize(databaseSizeBeforeCreate + 1);
        RecruitmentStep testRecruitmentStep = recruitmentStepList.get(recruitmentStepList.size() - 1);
        assertThat(testRecruitmentStep.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRecruitmentStep.getSequence()).isEqualTo(DEFAULT_SEQUENCE);
    }

    @Test
    @Transactional
    public void createRecruitmentStepWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = recruitmentStepRepository.findAll().size();

        // Create the RecruitmentStep with an existing ID
        recruitmentStep.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecruitmentStepMockMvc.perform(post("/api/recruitment-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruitmentStep)))
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentStep in the database
        List<RecruitmentStep> recruitmentStepList = recruitmentStepRepository.findAll();
        assertThat(recruitmentStepList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentStepRepository.findAll().size();
        // set the field null
        recruitmentStep.setDescription(null);

        // Create the RecruitmentStep, which fails.

        restRecruitmentStepMockMvc.perform(post("/api/recruitment-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruitmentStep)))
            .andExpect(status().isBadRequest());

        List<RecruitmentStep> recruitmentStepList = recruitmentStepRepository.findAll();
        assertThat(recruitmentStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSequenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = recruitmentStepRepository.findAll().size();
        // set the field null
        recruitmentStep.setSequence(null);

        // Create the RecruitmentStep, which fails.

        restRecruitmentStepMockMvc.perform(post("/api/recruitment-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruitmentStep)))
            .andExpect(status().isBadRequest());

        List<RecruitmentStep> recruitmentStepList = recruitmentStepRepository.findAll();
        assertThat(recruitmentStepList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRecruitmentSteps() throws Exception {
        // Initialize the database
        recruitmentStepRepository.saveAndFlush(recruitmentStep);

        // Get all the recruitmentStepList
        restRecruitmentStepMockMvc.perform(get("/api/recruitment-steps?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recruitmentStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)));
    }
    
    @Test
    @Transactional
    public void getRecruitmentStep() throws Exception {
        // Initialize the database
        recruitmentStepRepository.saveAndFlush(recruitmentStep);

        // Get the recruitmentStep
        restRecruitmentStepMockMvc.perform(get("/api/recruitment-steps/{id}", recruitmentStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(recruitmentStep.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.sequence").value(DEFAULT_SEQUENCE));
    }

    @Test
    @Transactional
    public void getNonExistingRecruitmentStep() throws Exception {
        // Get the recruitmentStep
        restRecruitmentStepMockMvc.perform(get("/api/recruitment-steps/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecruitmentStep() throws Exception {
        // Initialize the database
        recruitmentStepService.save(recruitmentStep);

        int databaseSizeBeforeUpdate = recruitmentStepRepository.findAll().size();

        // Update the recruitmentStep
        RecruitmentStep updatedRecruitmentStep = recruitmentStepRepository.findById(recruitmentStep.getId()).get();
        // Disconnect from session so that the updates on updatedRecruitmentStep are not directly saved in db
        em.detach(updatedRecruitmentStep);
        updatedRecruitmentStep
            .description(UPDATED_DESCRIPTION)
            .sequence(UPDATED_SEQUENCE);

        restRecruitmentStepMockMvc.perform(put("/api/recruitment-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRecruitmentStep)))
            .andExpect(status().isOk());

        // Validate the RecruitmentStep in the database
        List<RecruitmentStep> recruitmentStepList = recruitmentStepRepository.findAll();
        assertThat(recruitmentStepList).hasSize(databaseSizeBeforeUpdate);
        RecruitmentStep testRecruitmentStep = recruitmentStepList.get(recruitmentStepList.size() - 1);
        assertThat(testRecruitmentStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRecruitmentStep.getSequence()).isEqualTo(UPDATED_SEQUENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingRecruitmentStep() throws Exception {
        int databaseSizeBeforeUpdate = recruitmentStepRepository.findAll().size();

        // Create the RecruitmentStep

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecruitmentStepMockMvc.perform(put("/api/recruitment-steps")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(recruitmentStep)))
            .andExpect(status().isBadRequest());

        // Validate the RecruitmentStep in the database
        List<RecruitmentStep> recruitmentStepList = recruitmentStepRepository.findAll();
        assertThat(recruitmentStepList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRecruitmentStep() throws Exception {
        // Initialize the database
        recruitmentStepService.save(recruitmentStep);

        int databaseSizeBeforeDelete = recruitmentStepRepository.findAll().size();

        // Delete the recruitmentStep
        restRecruitmentStepMockMvc.perform(delete("/api/recruitment-steps/{id}", recruitmentStep.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecruitmentStep> recruitmentStepList = recruitmentStepRepository.findAll();
        assertThat(recruitmentStepList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecruitmentStep.class);
        RecruitmentStep recruitmentStep1 = new RecruitmentStep();
        recruitmentStep1.setId(1L);
        RecruitmentStep recruitmentStep2 = new RecruitmentStep();
        recruitmentStep2.setId(recruitmentStep1.getId());
        assertThat(recruitmentStep1).isEqualTo(recruitmentStep2);
        recruitmentStep2.setId(2L);
        assertThat(recruitmentStep1).isNotEqualTo(recruitmentStep2);
        recruitmentStep1.setId(null);
        assertThat(recruitmentStep1).isNotEqualTo(recruitmentStep2);
    }
}
