package ro.jobmat.web.rest;

import ro.jobmat.JobMatApp;
import ro.jobmat.domain.Opening;
import ro.jobmat.domain.City;
import ro.jobmat.domain.Company;
import ro.jobmat.repository.OpeningRepository;
import ro.jobmat.service.OpeningService;
import ro.jobmat.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static ro.jobmat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ro.jobmat.domain.enumeration.OpeningStatus;
/**
 * Integration tests for the {@link OpeningResource} REST controller.
 */
@SpringBootTest(classes = JobMatApp.class)
public class OpeningResourceIT {

    private static final OpeningStatus DEFAULT_STATUS = OpeningStatus.ACTIVE;
    private static final OpeningStatus UPDATED_STATUS = OpeningStatus.INACTIVE;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_J_D = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_J_D = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_J_D_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_J_D_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_POSITIONS_NO = 1;
    private static final Integer UPDATED_POSITIONS_NO = 2;
    private static final Integer SMALLER_POSITIONS_NO = 1 - 1;

    private static final String DEFAULT_MENTIONS = "AAAAAAAAAA";
    private static final String UPDATED_MENTIONS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PUBLIC_FOR_NON_COLLABORATORS = false;
    private static final Boolean UPDATED_PUBLIC_FOR_NON_COLLABORATORS = true;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_DATE = Instant.ofEpochMilli(-1L);

    @Autowired
    private OpeningRepository openingRepository;

    @Mock
    private OpeningRepository openingRepositoryMock;

    @Mock
    private OpeningService openingServiceMock;

    @Autowired
    private OpeningService openingService;

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

    private MockMvc restOpeningMockMvc;

    private Opening opening;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OpeningResource openingResource = new OpeningResource(openingService);
        this.restOpeningMockMvc = MockMvcBuilders.standaloneSetup(openingResource)
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
    public static Opening createEntity(EntityManager em) {
        Opening opening = new Opening()
            .status(DEFAULT_STATUS)
            .title(DEFAULT_TITLE)
            .jD(DEFAULT_J_D)
            .jDContentType(DEFAULT_J_D_CONTENT_TYPE)
            .positionsNo(DEFAULT_POSITIONS_NO)
            .mentions(DEFAULT_MENTIONS)
            .publicForNonCollaborators(DEFAULT_PUBLIC_FOR_NON_COLLABORATORS)
            .date(DEFAULT_DATE);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createEntity(em);
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        opening.setCity(city);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        opening.setCompany(company);
        return opening;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opening createUpdatedEntity(EntityManager em) {
        Opening opening = new Opening()
            .status(UPDATED_STATUS)
            .title(UPDATED_TITLE)
            .jD(UPDATED_J_D)
            .jDContentType(UPDATED_J_D_CONTENT_TYPE)
            .positionsNo(UPDATED_POSITIONS_NO)
            .mentions(UPDATED_MENTIONS)
            .publicForNonCollaborators(UPDATED_PUBLIC_FOR_NON_COLLABORATORS)
            .date(UPDATED_DATE);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createUpdatedEntity(em);
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        opening.setCity(city);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        opening.setCompany(company);
        return opening;
    }

    @BeforeEach
    public void initTest() {
        opening = createEntity(em);
    }

    @Test
    @Transactional
    public void createOpening() throws Exception {
        int databaseSizeBeforeCreate = openingRepository.findAll().size();

        // Create the Opening
        restOpeningMockMvc.perform(post("/api/openings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opening)))
            .andExpect(status().isCreated());

        // Validate the Opening in the database
        List<Opening> openingList = openingRepository.findAll();
        assertThat(openingList).hasSize(databaseSizeBeforeCreate + 1);
        Opening testOpening = openingList.get(openingList.size() - 1);
        assertThat(testOpening.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOpening.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testOpening.getjD()).isEqualTo(DEFAULT_J_D);
        assertThat(testOpening.getjDContentType()).isEqualTo(DEFAULT_J_D_CONTENT_TYPE);
        assertThat(testOpening.getPositionsNo()).isEqualTo(DEFAULT_POSITIONS_NO);
        assertThat(testOpening.getMentions()).isEqualTo(DEFAULT_MENTIONS);
        assertThat(testOpening.isPublicForNonCollaborators()).isEqualTo(DEFAULT_PUBLIC_FOR_NON_COLLABORATORS);
        assertThat(testOpening.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createOpeningWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = openingRepository.findAll().size();

        // Create the Opening with an existing ID
        opening.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpeningMockMvc.perform(post("/api/openings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opening)))
            .andExpect(status().isBadRequest());

        // Validate the Opening in the database
        List<Opening> openingList = openingRepository.findAll();
        assertThat(openingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingRepository.findAll().size();
        // set the field null
        opening.setStatus(null);

        // Create the Opening, which fails.

        restOpeningMockMvc.perform(post("/api/openings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opening)))
            .andExpect(status().isBadRequest());

        List<Opening> openingList = openingRepository.findAll();
        assertThat(openingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingRepository.findAll().size();
        // set the field null
        opening.setTitle(null);

        // Create the Opening, which fails.

        restOpeningMockMvc.perform(post("/api/openings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opening)))
            .andExpect(status().isBadRequest());

        List<Opening> openingList = openingRepository.findAll();
        assertThat(openingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPositionsNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingRepository.findAll().size();
        // set the field null
        opening.setPositionsNo(null);

        // Create the Opening, which fails.

        restOpeningMockMvc.perform(post("/api/openings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opening)))
            .andExpect(status().isBadRequest());

        List<Opening> openingList = openingRepository.findAll();
        assertThat(openingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublicForNonCollaboratorsIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingRepository.findAll().size();
        // set the field null
        opening.setPublicForNonCollaborators(null);

        // Create the Opening, which fails.

        restOpeningMockMvc.perform(post("/api/openings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opening)))
            .andExpect(status().isBadRequest());

        List<Opening> openingList = openingRepository.findAll();
        assertThat(openingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = openingRepository.findAll().size();
        // set the field null
        opening.setDate(null);

        // Create the Opening, which fails.

        restOpeningMockMvc.perform(post("/api/openings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opening)))
            .andExpect(status().isBadRequest());

        List<Opening> openingList = openingRepository.findAll();
        assertThat(openingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOpenings() throws Exception {
        // Initialize the database
        openingRepository.saveAndFlush(opening);

        // Get all the openingList
        restOpeningMockMvc.perform(get("/api/openings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opening.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].jDContentType").value(hasItem(DEFAULT_J_D_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].jD").value(hasItem(Base64Utils.encodeToString(DEFAULT_J_D))))
            .andExpect(jsonPath("$.[*].positionsNo").value(hasItem(DEFAULT_POSITIONS_NO)))
            .andExpect(jsonPath("$.[*].mentions").value(hasItem(DEFAULT_MENTIONS.toString())))
            .andExpect(jsonPath("$.[*].publicForNonCollaborators").value(hasItem(DEFAULT_PUBLIC_FOR_NON_COLLABORATORS.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllOpeningsWithEagerRelationshipsIsEnabled() throws Exception {
        OpeningResource openingResource = new OpeningResource(openingServiceMock);
        when(openingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restOpeningMockMvc = MockMvcBuilders.standaloneSetup(openingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOpeningMockMvc.perform(get("/api/openings?eagerload=true"))
        .andExpect(status().isOk());

        verify(openingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllOpeningsWithEagerRelationshipsIsNotEnabled() throws Exception {
        OpeningResource openingResource = new OpeningResource(openingServiceMock);
            when(openingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restOpeningMockMvc = MockMvcBuilders.standaloneSetup(openingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restOpeningMockMvc.perform(get("/api/openings?eagerload=true"))
        .andExpect(status().isOk());

            verify(openingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getOpening() throws Exception {
        // Initialize the database
        openingRepository.saveAndFlush(opening);

        // Get the opening
        restOpeningMockMvc.perform(get("/api/openings/{id}", opening.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(opening.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.jDContentType").value(DEFAULT_J_D_CONTENT_TYPE))
            .andExpect(jsonPath("$.jD").value(Base64Utils.encodeToString(DEFAULT_J_D)))
            .andExpect(jsonPath("$.positionsNo").value(DEFAULT_POSITIONS_NO))
            .andExpect(jsonPath("$.mentions").value(DEFAULT_MENTIONS.toString()))
            .andExpect(jsonPath("$.publicForNonCollaborators").value(DEFAULT_PUBLIC_FOR_NON_COLLABORATORS.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOpening() throws Exception {
        // Get the opening
        restOpeningMockMvc.perform(get("/api/openings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOpening() throws Exception {
        // Initialize the database
        openingService.save(opening);

        int databaseSizeBeforeUpdate = openingRepository.findAll().size();

        // Update the opening
        Opening updatedOpening = openingRepository.findById(opening.getId()).get();
        // Disconnect from session so that the updates on updatedOpening are not directly saved in db
        em.detach(updatedOpening);
        updatedOpening
            .status(UPDATED_STATUS)
            .title(UPDATED_TITLE)
            .jD(UPDATED_J_D)
            .jDContentType(UPDATED_J_D_CONTENT_TYPE)
            .positionsNo(UPDATED_POSITIONS_NO)
            .mentions(UPDATED_MENTIONS)
            .publicForNonCollaborators(UPDATED_PUBLIC_FOR_NON_COLLABORATORS)
            .date(UPDATED_DATE);

        restOpeningMockMvc.perform(put("/api/openings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOpening)))
            .andExpect(status().isOk());

        // Validate the Opening in the database
        List<Opening> openingList = openingRepository.findAll();
        assertThat(openingList).hasSize(databaseSizeBeforeUpdate);
        Opening testOpening = openingList.get(openingList.size() - 1);
        assertThat(testOpening.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOpening.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testOpening.getjD()).isEqualTo(UPDATED_J_D);
        assertThat(testOpening.getjDContentType()).isEqualTo(UPDATED_J_D_CONTENT_TYPE);
        assertThat(testOpening.getPositionsNo()).isEqualTo(UPDATED_POSITIONS_NO);
        assertThat(testOpening.getMentions()).isEqualTo(UPDATED_MENTIONS);
        assertThat(testOpening.isPublicForNonCollaborators()).isEqualTo(UPDATED_PUBLIC_FOR_NON_COLLABORATORS);
        assertThat(testOpening.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOpening() throws Exception {
        int databaseSizeBeforeUpdate = openingRepository.findAll().size();

        // Create the Opening

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpeningMockMvc.perform(put("/api/openings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(opening)))
            .andExpect(status().isBadRequest());

        // Validate the Opening in the database
        List<Opening> openingList = openingRepository.findAll();
        assertThat(openingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOpening() throws Exception {
        // Initialize the database
        openingService.save(opening);

        int databaseSizeBeforeDelete = openingRepository.findAll().size();

        // Delete the opening
        restOpeningMockMvc.perform(delete("/api/openings/{id}", opening.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Opening> openingList = openingRepository.findAll();
        assertThat(openingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opening.class);
        Opening opening1 = new Opening();
        opening1.setId(1L);
        Opening opening2 = new Opening();
        opening2.setId(opening1.getId());
        assertThat(opening1).isEqualTo(opening2);
        opening2.setId(2L);
        assertThat(opening1).isNotEqualTo(opening2);
        opening1.setId(null);
        assertThat(opening1).isNotEqualTo(opening2);
    }
}
