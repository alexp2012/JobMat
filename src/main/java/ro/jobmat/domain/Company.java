package ro.jobmat.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import ro.jobmat.domain.enumeration.CompanyType;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "company_type", nullable = false)
    private CompanyType companyType;

    @NotNull
    @Size(min = 2)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 4, max = 12)
    @Column(name = "cui", length = 12, nullable = false, unique = true)
    private String cui;

    @NotNull
    @Column(name = "join_date", nullable = false)
    private Instant joinDate;

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Opening> openings = new HashSet<>();

    @OneToMany(mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Candidate> candidates = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "company_city",
               joinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "city_id", referencedColumnName = "id"))
    private Set<City> cities = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "company_interest",
               joinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "interest_id", referencedColumnName = "id"))
    private Set<BusinessInterest> interests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public Company companyType(CompanyType companyType) {
        this.companyType = companyType;
        return this;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public String getName() {
        return name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCui() {
        return cui;
    }

    public Company cui(String cui) {
        this.cui = cui;
        return this;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public Instant getJoinDate() {
        return joinDate;
    }

    public Company joinDate(Instant joinDate) {
        this.joinDate = joinDate;
        return this;
    }

    public void setJoinDate(Instant joinDate) {
        this.joinDate = joinDate;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Company users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Company addUser(User user) {
        this.users.add(user);
        user.setCompany(this);
        return this;
    }

    public Company removeUser(User user) {
        this.users.remove(user);
        user.setCompany(null);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Opening> getOpenings() {
        return openings;
    }

    public Company openings(Set<Opening> openings) {
        this.openings = openings;
        return this;
    }

    public Company addOpening(Opening opening) {
        this.openings.add(opening);
        opening.setCompany(this);
        return this;
    }

    public Company removeOpening(Opening opening) {
        this.openings.remove(opening);
        opening.setCompany(null);
        return this;
    }

    public void setOpenings(Set<Opening> openings) {
        this.openings = openings;
    }

    public Set<Candidate> getCandidates() {
        return candidates;
    }

    public Company candidates(Set<Candidate> candidates) {
        this.candidates = candidates;
        return this;
    }

    public Company addCandidate(Candidate candidate) {
        this.candidates.add(candidate);
        candidate.setCompany(this);
        return this;
    }

    public Company removeCandidate(Candidate candidate) {
        this.candidates.remove(candidate);
        candidate.setCompany(null);
        return this;
    }

    public void setCandidates(Set<Candidate> candidates) {
        this.candidates = candidates;
    }

    public Set<City> getCities() {
        return cities;
    }

    public Company cities(Set<City> cities) {
        this.cities = cities;
        return this;
    }

    public Company addCity(City city) {
        this.cities.add(city);
        city.getCompanies().add(this);
        return this;
    }

    public Company removeCity(City city) {
        this.cities.remove(city);
        city.getCompanies().remove(this);
        return this;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    public Set<BusinessInterest> getInterests() {
        return interests;
    }

    public Company interests(Set<BusinessInterest> businessInterests) {
        this.interests = businessInterests;
        return this;
    }

    public Company addInterest(BusinessInterest businessInterest) {
        this.interests.add(businessInterest);
        businessInterest.getCompanies().add(this);
        return this;
    }

    public Company removeInterest(BusinessInterest businessInterest) {
        this.interests.remove(businessInterest);
        businessInterest.getCompanies().remove(this);
        return this;
    }

    public void setInterests(Set<BusinessInterest> businessInterests) {
        this.interests = businessInterests;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", companyType='" + getCompanyType() + "'" +
            ", name='" + getName() + "'" +
            ", cui='" + getCui() + "'" +
            ", joinDate='" + getJoinDate() + "'" +
            "}";
    }
}
