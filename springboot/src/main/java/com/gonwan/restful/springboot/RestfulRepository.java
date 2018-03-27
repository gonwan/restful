package com.gonwan.restful.springboot;

import java.security.GeneralSecurityException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gonwan.restful.springboot.model.TApi;
import com.gonwan.restful.springboot.model.TApiGroup;
import com.gonwan.restful.springboot.model.TAuthority;
import com.gonwan.restful.springboot.model.TDataSource;
import com.gonwan.restful.springboot.model.TUser;
import com.gonwan.restful.springboot.model.TUserDateCount;
import com.gonwan.restful.springboot.util.AesCryptography;

interface TApiRepository extends JpaRepository<TApi, Long> {
}

interface TApiGroupRepository extends JpaRepository<TApiGroup, Long> {
}

interface TAuthoritiesRepository extends JpaRepository<TAuthority, Long> {
}

interface TDataSourceRepository extends JpaRepository<TDataSource, Long> {
}

interface TUserRepository extends JpaRepository<TUser, String> {
}

interface TUserDateCountRepository extends JpaRepository<TUserDateCount, Long> {

    /* better use entity type here */
    List<TUserDateCount> findAllByRowDate(Date rowDate);

    @Transactional
    @Modifying
    @Query(value = "insert into t_user_date_count(username, row_date, row_count) values(:username, :row_date, :row_count) on duplicate key update row_count = row_count + :row_count", nativeQuery = true)
    void updateUserDateCount(@Param("username") String username, @Param("row_date") Date rowDate, @Param("row_count") Long rowCount);

}

/* JpaRepository classes generated from IntelliJ IDEA. */
/* @Repository is not necessary here, since all JpaRepository translates exceptions. */
@Repository
public class RestfulRepository {

    private static final Logger logger = LoggerFactory.getLogger(RestfulRepository.class);

    @Autowired
    private AesCryptography crypto;

    @Autowired
    private TApiRepository apiRepository;
    @Autowired
    private TApiGroupRepository apiGroupRepository;
    @Autowired
    private TAuthoritiesRepository authoritiesRepository;
    @Autowired
    private TDataSourceRepository dataSourceRepository;
    @Autowired
    private TUserRepository userRepository;
    @Autowired
    private TUserDateCountRepository userDateCountRepository;

    private volatile Collection<TApi> apis;
    private volatile Collection<TApiGroup> apiGroups;
    private volatile Collection<TAuthority> authorities;
    private volatile Collection<TDataSource> dataSources;
    private volatile Collection<TUser> users;
    private volatile Collection<TUserDateCount> userDateCounts;

    @PostConstruct
    public void init() {
        reload(RepoReloadFlag.ALL);
    }

    public void reload(RepoReloadFlag flags) {
        logger.info("Begin reload: {}", flags);
        try {
            if (flags.contains(RepoReloadFlag.API)) {
                apis = apiRepository.findAll();
            }
            if (flags.contains(RepoReloadFlag.API_GROUP)) {
                apiGroups = apiGroupRepository.findAll();
            }
            if (flags.contains(RepoReloadFlag.AUTHORITY)) {
                authorities = authoritiesRepository.findAll();
                authorities.stream().forEach(x -> {
                    if (x.getMaxRowOnce() == null) {
                        x.setMaxRowOnce(0L);
                    }
                    if (x.getMaxRowDaily() == null) {
                        x.setMaxRowDaily(0L);
                    }
                });
            }
            if (flags.contains(RepoReloadFlag.DATA_SOURCE)) {
                dataSources = dataSourceRepository.findAll();
                for (TDataSource ds : dataSources) {
                    try {
                        String password = StringUtils.trimToEmpty(ds.getReadPassword());
                        if (!password.isEmpty()) {
                            password = crypto.Decrypt(password);
                            ds.setReadPassword(password);
                        }
                    } catch (GeneralSecurityException e) {
                        logger.warn("", e);
                    }
                    try {
                        String password = StringUtils.trimToEmpty(ds.getWritePassword());
                        if (!password.isEmpty()) {
                            password = crypto.Decrypt(password);
                            ds.setWritePassword(password);
                        }
                    } catch (GeneralSecurityException e) {
                        logger.warn("", e);
                    }
                }
            }
            if (flags.contains(RepoReloadFlag.USER)) {
                users = userRepository.findAll();
            }
            if (flags.contains(RepoReloadFlag.USER_DATA_COUNT)) {
                userDateCounts = userDateCountRepository.findAllByRowDate(Date.valueOf(LocalDate.now()));
            }
        } catch (DataAccessException e) {
            logger.warn("Reload failed", e);
        }
        logger.info("End reload: {}", flags);
    }

    public Collection<TApi> getApis() {
        return apis;
    }

    public Collection<TApiGroup> getApiGroups() {
        return apiGroups;
    }

    public Collection<TAuthority> getAuthorities() {
        return authorities;
    }

    public Collection<TDataSource> getDataSources() {
        return dataSources;
    }

    public Collection<TUser> getUsers() {
        return users;
    }

    public Collection<TUserDateCount> getUserDateCounts() {
        return userDateCounts;
    }

    public boolean updateUserCountToday(String username, Long rowCount) {
        try {
            userDateCountRepository.updateUserDateCount(username, Date.valueOf(LocalDate.now()), rowCount);
        } catch (DataAccessException e) {
            logger.info("", e);
            return false;
        }
        return true;
    }

}
