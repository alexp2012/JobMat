package ro.jobmat.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @OneToMany(mappedBy = "application")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ApplicationMessage> messages = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("applications")
    private RecruitmentStep step;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("applications")
    private Candidate candidate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public Application date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Set<ApplicationMessage> getMessages() {
        return messages;
    }

    public Application messages(Set<ApplicationMessage> applicationMessages) {
        this.messages = applicationMessages;
        return this;
    }

    public Application addMessage(ApplicationMessage applicationMessage) {
        this.messages.add(applicationMessage);
        applicationMessage.setApplication(this);
        return this;
    }

    public Application removeMessage(ApplicationMessage applicationMessage) {
        this.messages.remove(applicationMessage);
        applicationMessage.setApplication(null);
        return this;
    }

    public void setMessages(Set<ApplicationMessage> applicationMessages) {
        this.messages = applicationMessages;
    }

    public RecruitmentStep getStep() {
        return step;
    }

    public Application step(RecruitmentStep recruitmentStep) {
        this.step = recruitmentStep;
        return this;
    }

    public void setStep(RecruitmentStep recruitmentStep) {
        this.step = recruitmentStep;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public Application candidate(Candidate candidate) {
        this.candidate = candidate;
        return this;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Application)) {
            return false;
        }
        return id != null && id.equals(((Application) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
