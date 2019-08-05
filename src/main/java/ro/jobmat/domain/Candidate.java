package ro.jobmat.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Candidate.
 */
@Entity
@Table(name = "candidate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 2)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Lob
    @Column(name = "c_v")
    private byte[] cV;

    @Column(name = "c_v_content_type")
    private String cVContentType;

    @Column(name = "mentions")
    private String mentions;

    @Column(name = "expected_salary_eur")
    private Integer expectedSalaryEur;

    @OneToMany(mappedBy = "candidate")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Application> applications = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("candidates")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Candidate firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Candidate lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Candidate email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Candidate phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getcV() {
        return cV;
    }

    public Candidate cV(byte[] cV) {
        this.cV = cV;
        return this;
    }

    public void setcV(byte[] cV) {
        this.cV = cV;
    }

    public String getcVContentType() {
        return cVContentType;
    }

    public Candidate cVContentType(String cVContentType) {
        this.cVContentType = cVContentType;
        return this;
    }

    public void setcVContentType(String cVContentType) {
        this.cVContentType = cVContentType;
    }

    public String getMentions() {
        return mentions;
    }

    public Candidate mentions(String mentions) {
        this.mentions = mentions;
        return this;
    }

    public void setMentions(String mentions) {
        this.mentions = mentions;
    }

    public Integer getExpectedSalaryEur() {
        return expectedSalaryEur;
    }

    public Candidate expectedSalaryEur(Integer expectedSalaryEur) {
        this.expectedSalaryEur = expectedSalaryEur;
        return this;
    }

    public void setExpectedSalaryEur(Integer expectedSalaryEur) {
        this.expectedSalaryEur = expectedSalaryEur;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public Candidate applications(Set<Application> applications) {
        this.applications = applications;
        return this;
    }

    public Candidate addApplication(Application application) {
        this.applications.add(application);
        application.setCandidate(this);
        return this;
    }

    public Candidate removeApplication(Application application) {
        this.applications.remove(application);
        application.setCandidate(null);
        return this;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Company getCompany() {
        return company;
    }

    public Candidate company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Candidate)) {
            return false;
        }
        return id != null && id.equals(((Candidate) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Candidate{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", cV='" + getcV() + "'" +
            ", cVContentType='" + getcVContentType() + "'" +
            ", mentions='" + getMentions() + "'" +
            ", expectedSalaryEur=" + getExpectedSalaryEur() +
            "}";
    }
}
