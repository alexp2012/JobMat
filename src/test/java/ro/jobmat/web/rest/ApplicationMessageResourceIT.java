package ro.jobmat.web.rest;

import ro.jobmat.JobMatApp;
import ro.jobmat.domain.ApplicationMessage;
import ro.jobmat.domain.Company;
import ro.jobmat.domain.Application;
import ro.jobmat.repository.ApplicationMessageRepository;
import ro.jobmat.service.ApplicationMessageService;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ro.jobmat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ApplicationMessageResource} REST controller.
 */
@SpringBootTest(classes = JobMatApp.class)
public class ApplicationMessageResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_DATE = Instant.ofEpochMilli(-1L);

    @Autowired
    private ApplicationMessageRepository applicationMessageRepository;

    @Autowired
    private ApplicationMessageService applicationMessageService;

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

    private MockMvc restApplicationMessageMockMvc;

    private ApplicationMessage applicationMessage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationMessageResource applicationMessageResource = new ApplicationMessageResource(applicationMessageService);
        this.restApplicationMessageMockMvc = MockMvcBuilders.standaloneSetup(applicationMessageResource)
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
    public static ApplicationMessage createEntity(EntityManager em) {
        ApplicationMessage applicationMessage = new ApplicationMessage()
            .text(DEFAULT_TEXT)
            .date(DEFAULT_DATE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        applicationMessage.setCompany(company);
        // Add required entity
        Application application;
        if (TestUtil.findAll(em, Application.class).isEmpty()) {
            application = ApplicationResourceIT.createEntity(em);
            em.persist(application);
            em.flush();
        } else {
            application = TestUtil.findAll(em, Application.class).get(0);
        }
        applicationMessage.setApplication(application);
        return applicationMessage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationMessage createUpdatedEntity(EntityManager em) {
        ApplicationMessage applicationMessage = new ApplicationMessage()
            .text(UPDATED_TEXT)
            .date(UPDATED_DATE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        applicationMessage.setCompany(company);
        // Add required entity
        Application application;
        if (TestUtil.findAll(em, Application.class).isEmpty()) {
            application = ApplicationResourceIT.createUpdatedEntity(em);
            em.persist(application);
            em.flush();
        } else {
            application = TestUtil.findAll(em, Application.class).get(0);
        }
        applicationMessage.setApplication(application);
        return applicationMessage;
    }

    @BeforeEach
    public void initTest() {
        applicationMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationMessage() throws Exception {
        int databaseSizeBeforeCreate = applicationMessageRepository.findAll().size();

        // Create the ApplicationMessage
        restApplicationMessageMockMvc.perform(post("/api/application-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMessage)))
            .andExpect(status().isCreated());

        // Validate the ApplicationMessage in the database
        List<ApplicationMessage> applicationMessageList = applicationMessageRepository.findAll();
        assertThat(applicationMessageList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationMessage testApplicationMessage = applicationMessageList.get(applicationMessageList.size() - 1);
        assertThat(testApplicationMessage.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testApplicationMessage.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createApplicationMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationMessageRepository.findAll().size();

        // Create the ApplicationMessage with an existing ID
        applicationMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMessageMockMvc.perform(post("/api/application-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMessage)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationMessage in the database
        List<ApplicationMessage> applicationMessageList = applicationMessageRepository.findAll();
        assertThat(applicationMessageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMessageRepository.findAll().size();
        // set the field null
        applicationMessage.setText(null);

        // Create the ApplicationMessage, which fails.

        restApplicationMessageMockMvc.perform(post("/api/application-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMessage)))
            .andExpect(status().isBadRequest());

        List<ApplicationMessage> applicationMessageList = applicationMessageRepository.findAll();
        assertThat(applicationMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMessageRepository.findAll().size();
        // set the field null
        applicationMessage.setDate(null);

        // Create the ApplicationMessage, which fails.

        restApplicationMessageMockMvc.perform(post("/api/application-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMessage)))
            .andExpect(status().isBadRequest());

        List<ApplicationMessage> applicationMessageList = applicationMessageRepository.findAll();
        assertThat(applicationMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicationMessages() throws Exception {
        // Initialize the database
        applicationMessageRepository.saveAndFlush(applicationMessage);

        // Get all the applicationMessageList
        restApplicationMessageMockMvc.perform(get("/api/application-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getApplicationMessage() throws Exception {
        // Initialize the database
        applicationMessageRepository.saveAndFlush(applicationMessage);

        // Get the applicationMessage
        restApplicationMessageMockMvc.perform(get("/api/application-messages/{id}", applicationMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applicationMessage.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationMessage() throws Exception {
        // Get the applicationMessage
        restApplicationMessageMockMvc.perform(get("/api/application-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationMessage() throws Exception {
        // Initialize the database
        applicationMessageService.save(applicationMessage);

        int databaseSizeBeforeUpdate = applicationMessageRepository.findAll().size();

        // Update the applicationMessage
        ApplicationMessage updatedApplicationMessage = applicationMessageRepository.findById(applicationMessage.getId()).get();
        // Disconnect from session so that the updates on updatedApplicationMessage are not directly saved in db
        em.detach(updatedApplicationMessage);
        updatedApplicationMessage
            .text(UPDATED_TEXT)
            .date(UPDATED_DATE);

        restApplicationMessageMockMvc.perform(put("/api/application-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApplicationMessage)))
            .andExpect(status().isOk());

        // Validate the ApplicationMessage in the database
        List<ApplicationMessage> applicationMessageList = applicationMessageRepository.findAll();
        assertThat(applicationMessageList).hasSize(databaseSizeBeforeUpdate);
        ApplicationMessage testApplicationMessage = applicationMessageList.get(applicationMessageList.size() - 1);
        assertThat(testApplicationMessage.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testApplicationMessage.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationMessage() throws Exception {
        int databaseSizeBeforeUpdate = applicationMessageRepository.findAll().size();

        // Create the ApplicationMessage

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationMessageMockMvc.perform(put("/api/application-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMessage)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationMessage in the database
        List<ApplicationMessage> applicationMessageList = applicationMessageRepository.findAll();
        assertThat(applicationMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApplicationMessage() throws Exception {
        // Initialize the database
        applicationMessageService.save(applicationMessage);

        int databaseSizeBeforeDelete = applicationMessageRepository.findAll().size();

        // Delete the applicationMessage
        restApplicationMessageMockMvc.perform(delete("/api/application-messages/{id}", applicationMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationMessage> applicationMessageList = applicationMessageRepository.findAll();
        assertThat(applicationMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationMessage.class);
        ApplicationMessage applicationMessage1 = new ApplicationMessage();
        applicationMessage1.setId(1L);
        ApplicationMessage applicationMessage2 = new ApplicationMessage();
        applicationMessage2.setId(applicationMessage1.getId());
        assertThat(applicationMessage1).isEqualTo(applicationMessage2);
        applicationMessage2.setId(2L);
        assertThat(applicationMessage1).isNotEqualTo(applicationMessage2);
        applicationMessage1.setId(null);
        assertThat(applicationMessage1).isNotEqualTo(applicationMessage2);
    }
}
