package ro.jobmat.web.rest;

import ro.jobmat.JobMatApp;
import ro.jobmat.domain.BusinessInterest;
import ro.jobmat.repository.BusinessInterestRepository;
import ro.jobmat.service.BusinessInterestService;
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
 * Integration tests for the {@link BusinessInterestResource} REST controller.
 */
@SpringBootTest(classes = JobMatApp.class)
public class BusinessInterestResourceIT {

    private static final String DEFAULT_INTEREST = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST = "BBBBBBBBBB";

    @Autowired
    private BusinessInterestRepository businessInterestRepository;

    @Autowired
    private BusinessInterestService businessInterestService;

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

    private MockMvc restBusinessInterestMockMvc;

    private BusinessInterest businessInterest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BusinessInterestResource businessInterestResource = new BusinessInterestResource(businessInterestService);
        this.restBusinessInterestMockMvc = MockMvcBuilders.standaloneSetup(businessInterestResource)
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
    public static BusinessInterest createEntity(EntityManager em) {
        BusinessInterest businessInterest = new BusinessInterest()
            .interest(DEFAULT_INTEREST);
        return businessInterest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessInterest createUpdatedEntity(EntityManager em) {
        BusinessInterest businessInterest = new BusinessInterest()
            .interest(UPDATED_INTEREST);
        return businessInterest;
    }

    @BeforeEach
    public void initTest() {
        businessInterest = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusinessInterest() throws Exception {
        int databaseSizeBeforeCreate = businessInterestRepository.findAll().size();

        // Create the BusinessInterest
        restBusinessInterestMockMvc.perform(post("/api/business-interests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessInterest)))
            .andExpect(status().isCreated());

        // Validate the BusinessInterest in the database
        List<BusinessInterest> businessInterestList = businessInterestRepository.findAll();
        assertThat(businessInterestList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessInterest testBusinessInterest = businessInterestList.get(businessInterestList.size() - 1);
        assertThat(testBusinessInterest.getInterest()).isEqualTo(DEFAULT_INTEREST);
    }

    @Test
    @Transactional
    public void createBusinessInterestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = businessInterestRepository.findAll().size();

        // Create the BusinessInterest with an existing ID
        businessInterest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessInterestMockMvc.perform(post("/api/business-interests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessInterest)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessInterest in the database
        List<BusinessInterest> businessInterestList = businessInterestRepository.findAll();
        assertThat(businessInterestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkInterestIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessInterestRepository.findAll().size();
        // set the field null
        businessInterest.setInterest(null);

        // Create the BusinessInterest, which fails.

        restBusinessInterestMockMvc.perform(post("/api/business-interests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessInterest)))
            .andExpect(status().isBadRequest());

        List<BusinessInterest> businessInterestList = businessInterestRepository.findAll();
        assertThat(businessInterestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBusinessInterests() throws Exception {
        // Initialize the database
        businessInterestRepository.saveAndFlush(businessInterest);

        // Get all the businessInterestList
        restBusinessInterestMockMvc.perform(get("/api/business-interests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessInterest.getId().intValue())))
            .andExpect(jsonPath("$.[*].interest").value(hasItem(DEFAULT_INTEREST.toString())));
    }
    
    @Test
    @Transactional
    public void getBusinessInterest() throws Exception {
        // Initialize the database
        businessInterestRepository.saveAndFlush(businessInterest);

        // Get the businessInterest
        restBusinessInterestMockMvc.perform(get("/api/business-interests/{id}", businessInterest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(businessInterest.getId().intValue()))
            .andExpect(jsonPath("$.interest").value(DEFAULT_INTEREST.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBusinessInterest() throws Exception {
        // Get the businessInterest
        restBusinessInterestMockMvc.perform(get("/api/business-interests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessInterest() throws Exception {
        // Initialize the database
        businessInterestService.save(businessInterest);

        int databaseSizeBeforeUpdate = businessInterestRepository.findAll().size();

        // Update the businessInterest
        BusinessInterest updatedBusinessInterest = businessInterestRepository.findById(businessInterest.getId()).get();
        // Disconnect from session so that the updates on updatedBusinessInterest are not directly saved in db
        em.detach(updatedBusinessInterest);
        updatedBusinessInterest
            .interest(UPDATED_INTEREST);

        restBusinessInterestMockMvc.perform(put("/api/business-interests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBusinessInterest)))
            .andExpect(status().isOk());

        // Validate the BusinessInterest in the database
        List<BusinessInterest> businessInterestList = businessInterestRepository.findAll();
        assertThat(businessInterestList).hasSize(databaseSizeBeforeUpdate);
        BusinessInterest testBusinessInterest = businessInterestList.get(businessInterestList.size() - 1);
        assertThat(testBusinessInterest.getInterest()).isEqualTo(UPDATED_INTEREST);
    }

    @Test
    @Transactional
    public void updateNonExistingBusinessInterest() throws Exception {
        int databaseSizeBeforeUpdate = businessInterestRepository.findAll().size();

        // Create the BusinessInterest

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessInterestMockMvc.perform(put("/api/business-interests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessInterest)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessInterest in the database
        List<BusinessInterest> businessInterestList = businessInterestRepository.findAll();
        assertThat(businessInterestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBusinessInterest() throws Exception {
        // Initialize the database
        businessInterestService.save(businessInterest);

        int databaseSizeBeforeDelete = businessInterestRepository.findAll().size();

        // Delete the businessInterest
        restBusinessInterestMockMvc.perform(delete("/api/business-interests/{id}", businessInterest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessInterest> businessInterestList = businessInterestRepository.findAll();
        assertThat(businessInterestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessInterest.class);
        BusinessInterest businessInterest1 = new BusinessInterest();
        businessInterest1.setId(1L);
        BusinessInterest businessInterest2 = new BusinessInterest();
        businessInterest2.setId(businessInterest1.getId());
        assertThat(businessInterest1).isEqualTo(businessInterest2);
        businessInterest2.setId(2L);
        assertThat(businessInterest1).isNotEqualTo(businessInterest2);
        businessInterest1.setId(null);
        assertThat(businessInterest1).isNotEqualTo(businessInterest2);
    }
}
