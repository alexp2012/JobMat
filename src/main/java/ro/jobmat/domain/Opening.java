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

import ro.jobmat.domain.enumeration.OpeningStatus;

/**
 * A Opening.
 */
@Entity
@Table(name = "opening")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Opening implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OpeningStatus status;

    @NotNull
    @Size(min = 2)
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "j_d")
    private byte[] jD;

    @Column(name = "j_d_content_type")
    private String jDContentType;

    @NotNull
    @Min(value = 1)
    @Max(value = 999)
    @Column(name = "positions_no", nullable = false)
    private Integer positionsNo;

    @Column(name = "mentions")
    private String mentions;

    @NotNull
    @Column(name = "public_for_non_collaborators", nullable = false)
    private Boolean publicForNonCollaborators;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @OneToMany(mappedBy = "opening")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecruitmentStep> steps = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("openings")
    private City city;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "opening_tag",
               joinColumns = @JoinColumn(name = "opening_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("openings")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OpeningStatus getStatus() {
        return status;
    }

    public Opening status(OpeningStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OpeningStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public Opening title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getjD() {
        return jD;
    }

    public Opening jD(byte[] jD) {
        this.jD = jD;
        return this;
    }

    public void setjD(byte[] jD) {
        this.jD = jD;
    }

    public String getjDContentType() {
        return jDContentType;
    }

    public Opening jDContentType(String jDContentType) {
        this.jDContentType = jDContentType;
        return this;
    }

    public void setjDContentType(String jDContentType) {
        this.jDContentType = jDContentType;
    }

    public Integer getPositionsNo() {
        return positionsNo;
    }

    public Opening positionsNo(Integer positionsNo) {
        this.positionsNo = positionsNo;
        return this;
    }

    public void setPositionsNo(Integer positionsNo) {
        this.positionsNo = positionsNo;
    }

    public String getMentions() {
        return mentions;
    }

    public Opening mentions(String mentions) {
        this.mentions = mentions;
        return this;
    }

    public void setMentions(String mentions) {
        this.mentions = mentions;
    }

    public Boolean isPublicForNonCollaborators() {
        return publicForNonCollaborators;
    }

    public Opening publicForNonCollaborators(Boolean publicForNonCollaborators) {
        this.publicForNonCollaborators = publicForNonCollaborators;
        return this;
    }

    public void setPublicForNonCollaborators(Boolean publicForNonCollaborators) {
        this.publicForNonCollaborators = publicForNonCollaborators;
    }

    public Instant getDate() {
        return date;
    }

    public Opening date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Set<RecruitmentStep> getSteps() {
        return steps;
    }

    public Opening steps(Set<RecruitmentStep> recruitmentSteps) {
        this.steps = recruitmentSteps;
        return this;
    }

    public Opening addStep(RecruitmentStep recruitmentStep) {
        this.steps.add(recruitmentStep);
        recruitmentStep.setOpening(this);
        return this;
    }

    public Opening removeStep(RecruitmentStep recruitmentStep) {
        this.steps.remove(recruitmentStep);
        recruitmentStep.setOpening(null);
        return this;
    }

    public void setSteps(Set<RecruitmentStep> recruitmentSteps) {
        this.steps = recruitmentSteps;
    }

    public City getCity() {
        return city;
    }

    public Opening city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Opening tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Opening addTag(Tag tag) {
        this.tags.add(tag);
        tag.getOpenings().add(this);
        return this;
    }

    public Opening removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getOpenings().remove(this);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Company getCompany() {
        return company;
    }

    public Opening company(Company company) {
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
        if (!(o instanceof Opening)) {
            return false;
        }
        return id != null && id.equals(((Opening) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Opening{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", title='" + getTitle() + "'" +
            ", jD='" + getjD() + "'" +
            ", jDContentType='" + getjDContentType() + "'" +
            ", positionsNo=" + getPositionsNo() +
            ", mentions='" + getMentions() + "'" +
            ", publicForNonCollaborators='" + isPublicForNonCollaborators() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
