package ro.jobmat.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import ro.jobmat.domain.enumeration.CollaborationStatus;

import ro.jobmat.domain.enumeration.CompanyType;

/**
 * A Collaboration.
 */
@Entity
@Table(name = "collaboration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Collaboration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CollaborationStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "initiator", nullable = false)
    private CompanyType initiator;

    @NotNull
    @Column(name = "invitations_no", nullable = false)
    private Integer invitationsNo;

    @NotNull
    @Size(min = 20)
    @Column(name = "message", nullable = false)
    private String message;

    @Lob
    @Column(name = "contract")
    private byte[] contract;

    @Column(name = "contract_content_type")
    private String contractContentType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("collaborations")
    private Company supplier;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("collaborations")
    private Company customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CollaborationStatus getStatus() {
        return status;
    }

    public Collaboration status(CollaborationStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CollaborationStatus status) {
        this.status = status;
    }

    public CompanyType getInitiator() {
        return initiator;
    }

    public Collaboration initiator(CompanyType initiator) {
        this.initiator = initiator;
        return this;
    }

    public void setInitiator(CompanyType initiator) {
        this.initiator = initiator;
    }

    public Integer getInvitationsNo() {
        return invitationsNo;
    }

    public Collaboration invitationsNo(Integer invitationsNo) {
        this.invitationsNo = invitationsNo;
        return this;
    }

    public void setInvitationsNo(Integer invitationsNo) {
        this.invitationsNo = invitationsNo;
    }

    public String getMessage() {
        return message;
    }

    public Collaboration message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getContract() {
        return contract;
    }

    public Collaboration contract(byte[] contract) {
        this.contract = contract;
        return this;
    }

    public void setContract(byte[] contract) {
        this.contract = contract;
    }

    public String getContractContentType() {
        return contractContentType;
    }

    public Collaboration contractContentType(String contractContentType) {
        this.contractContentType = contractContentType;
        return this;
    }

    public void setContractContentType(String contractContentType) {
        this.contractContentType = contractContentType;
    }

    public Company getSupplier() {
        return supplier;
    }

    public Collaboration supplier(Company company) {
        this.supplier = company;
        return this;
    }

    public void setSupplier(Company company) {
        this.supplier = company;
    }

    public Company getCustomer() {
        return customer;
    }

    public Collaboration customer(Company company) {
        this.customer = company;
        return this;
    }

    public void setCustomer(Company company) {
        this.customer = company;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Collaboration)) {
            return false;
        }
        return id != null && id.equals(((Collaboration) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Collaboration{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", initiator='" + getInitiator() + "'" +
            ", invitationsNo=" + getInvitationsNo() +
            ", message='" + getMessage() + "'" +
            ", contract='" + getContract() + "'" +
            ", contractContentType='" + getContractContentType() + "'" +
            "}";
    }
}
