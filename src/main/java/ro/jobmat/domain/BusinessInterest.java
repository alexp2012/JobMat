package ro.jobmat.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A BusinessInterest.
 */
@Entity
@Table(name = "business_interest")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BusinessInterest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "interest", nullable = false, unique = true)
    private String interest;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInterest() {
        return interest;
    }

    public BusinessInterest interest(String interest) {
        this.interest = interest;
        return this;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BusinessInterest)) {
            return false;
        }
        return id != null && id.equals(((BusinessInterest) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BusinessInterest{" +
            "id=" + getId() +
            ", interest='" + getInterest() + "'" +
            "}";
    }
}
