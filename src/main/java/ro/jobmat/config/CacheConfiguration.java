package ro.jobmat.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, ro.jobmat.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, ro.jobmat.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, ro.jobmat.domain.User.class.getName());
            createCache(cm, ro.jobmat.domain.Authority.class.getName());
            createCache(cm, ro.jobmat.domain.User.class.getName() + ".authorities");
            createCache(cm, ro.jobmat.domain.City.class.getName());
            createCache(cm, ro.jobmat.domain.BusinessInterest.class.getName());
            createCache(cm, ro.jobmat.domain.Company.class.getName());
            createCache(cm, ro.jobmat.domain.Company.class.getName() + ".users");
            createCache(cm, ro.jobmat.domain.Company.class.getName() + ".openings");
            createCache(cm, ro.jobmat.domain.Company.class.getName() + ".candidates");
            createCache(cm, ro.jobmat.domain.Company.class.getName() + ".cities");
            createCache(cm, ro.jobmat.domain.Company.class.getName() + ".interests");
            createCache(cm, ro.jobmat.domain.Candidate.class.getName());
            createCache(cm, ro.jobmat.domain.Candidate.class.getName() + ".applications");
            createCache(cm, ro.jobmat.domain.Opening.class.getName());
            createCache(cm, ro.jobmat.domain.Opening.class.getName() + ".steps");
            createCache(cm, ro.jobmat.domain.Opening.class.getName() + ".tags");
            createCache(cm, ro.jobmat.domain.Tag.class.getName());
            createCache(cm, ro.jobmat.domain.RecruitmentStep.class.getName());
            createCache(cm, ro.jobmat.domain.RecruitmentStep.class.getName() + ".applications");
            createCache(cm, ro.jobmat.domain.Collaboration.class.getName());
            createCache(cm, ro.jobmat.domain.ApplicationMessage.class.getName());
            createCache(cm, ro.jobmat.domain.Application.class.getName());
            createCache(cm, ro.jobmat.domain.Application.class.getName() + ".messages");
            createCache(cm, ro.jobmat.domain.ExtendedUser.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }
}
