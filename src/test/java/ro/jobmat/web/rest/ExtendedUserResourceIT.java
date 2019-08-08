package ro.jobmat.web.rest;

import ro.jobmat.JobMatApp;
import ro.jobmat.domain.ExtendedUser;
import ro.jobmat.repository.ExtendedUserRepository;
import ro.jobmat.service.ExtendedUserService;
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
 * Integration tests for the {@link ExtendedUserResource} REST controller.
 */
@SpringBootTest(classes = JobMatApp.class)
public class ExtendedUserResourceIT {

    @Autowired
    private ExtendedUserRepository extendedUserRepository;

    @Autowired
    private ExtendedUserService extendedUserService;

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

    private MockMvc restExtendedUserMockMvc;

    private ExtendedUser extendedUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExtendedUserResource extendedUserResource = new ExtendedUserResource(extendedUserService);
        this.restExtendedUserMockMvc = MockMvcBuilders.standaloneSetup(extendedUserResource)
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
    public static ExtendedUser createEntity(EntityManager em) {
        ExtendedUser extendedUser = new ExtendedUser();
        return extendedUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtendedUser createUpdatedEntity(EntityManager em) {
        ExtendedUser extendedUser = new ExtendedUser();
        return extendedUser;
    }

    @BeforeEach
    public void initTest() {
        extendedUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtendedUser() throws Exception {
        int databaseSizeBeforeCreate = extendedUserRepository.findAll().size();

        // Create the ExtendedUser
        restExtendedUserMockMvc.perform(post("/api/extended-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extendedUser)))
            .andExpect(status().isCreated());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeCreate + 1);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
    }

    @Test
    @Transactional
    public void createExtendedUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extendedUserRepository.findAll().size();

        // Create the ExtendedUser with an existing ID
        extendedUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtendedUserMockMvc.perform(post("/api/extended-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extendedUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExtendedUsers() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get all the extendedUserList
        restExtendedUserMockMvc.perform(get("/api/extended-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extendedUser.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getExtendedUser() throws Exception {
        // Initialize the database
        extendedUserRepository.saveAndFlush(extendedUser);

        // Get the extendedUser
        restExtendedUserMockMvc.perform(get("/api/extended-users/{id}", extendedUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extendedUser.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExtendedUser() throws Exception {
        // Get the extendedUser
        restExtendedUserMockMvc.perform(get("/api/extended-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtendedUser() throws Exception {
        // Initialize the database
        extendedUserService.save(extendedUser);

        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();

        // Update the extendedUser
        ExtendedUser updatedExtendedUser = extendedUserRepository.findById(extendedUser.getId()).get();
        // Disconnect from session so that the updates on updatedExtendedUser are not directly saved in db
        em.detach(updatedExtendedUser);

        restExtendedUserMockMvc.perform(put("/api/extended-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtendedUser)))
            .andExpect(status().isOk());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
        ExtendedUser testExtendedUser = extendedUserList.get(extendedUserList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingExtendedUser() throws Exception {
        int databaseSizeBeforeUpdate = extendedUserRepository.findAll().size();

        // Create the ExtendedUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtendedUserMockMvc.perform(put("/api/extended-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extendedUser)))
            .andExpect(status().isBadRequest());

        // Validate the ExtendedUser in the database
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExtendedUser() throws Exception {
        // Initialize the database
        extendedUserService.save(extendedUser);

        int databaseSizeBeforeDelete = extendedUserRepository.findAll().size();

        // Delete the extendedUser
        restExtendedUserMockMvc.perform(delete("/api/extended-users/{id}", extendedUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExtendedUser> extendedUserList = extendedUserRepository.findAll();
        assertThat(extendedUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtendedUser.class);
        ExtendedUser extendedUser1 = new ExtendedUser();
        extendedUser1.setId(1L);
        ExtendedUser extendedUser2 = new ExtendedUser();
        extendedUser2.setId(extendedUser1.getId());
        assertThat(extendedUser1).isEqualTo(extendedUser2);
        extendedUser2.setId(2L);
        assertThat(extendedUser1).isNotEqualTo(extendedUser2);
        extendedUser1.setId(null);
        assertThat(extendedUser1).isNotEqualTo(extendedUser2);
    }
}
