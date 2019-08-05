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
 * A RecruitmentStep.
 */
@Entity
@Table(name = "recruitment_step")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecruitmentStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 10)
    @Column(name = "description", length = 10, nullable = false)
    private String description;

    @NotNull
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @OneToMany(mappedBy = "step")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Application> applications = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("steps")
    private Opening opening;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public RecruitmentStep description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSequence() {
        return sequence;
    }

    public RecruitmentStep sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public RecruitmentStep applications(Set<Application> applications) {
        this.applications = applications;
        return this;
    }

    public RecruitmentStep addApplication(Application application) {
        this.applications.add(application);
        application.setStep(this);
        return this;
    }

    public RecruitmentStep removeApplication(Application application) {
        this.applications.remove(application);
        application.setStep(null);
        return this;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }

    public Opening getOpening() {
        return opening;
    }

    public RecruitmentStep opening(Opening opening) {
        this.opening = opening;
        return this;
    }

    public void setOpening(Opening opening) {
        this.opening = opening;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecruitmentStep)) {
            return false;
        }
        return id != null && id.equals(((RecruitmentStep) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RecruitmentStep{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", sequence=" + getSequence() +
            "}";
    }
}
