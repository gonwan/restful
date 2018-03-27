package com.gonwan.restful.springboot.test;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gonwan.restful.springboot.RestfulRepository;
import com.gonwan.restful.springboot.SpringConfiguration;
import com.gonwan.restful.springboot.model.TApi;
import com.gonwan.restful.springboot.model.TApiGroup;
import com.gonwan.restful.springboot.model.TAuthority;
import com.gonwan.restful.springboot.model.TDataSource;
import com.gonwan.restful.springboot.model.TUser;
import com.gonwan.restful.springboot.model.TUserDateCount;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class)
public class DbRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(DbRepositoryTest.class);

    @Autowired
    private RestfulRepository repo;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDb() {
        Collection<TApi> apis = repo.getApis();
        logger.info("apis size = " + apis.size());
        assertThat(apis.size(), not(0));
        Collection<TApiGroup> apiGroups = repo.getApiGroups();
        logger.info("apiGroups size = " + apiGroups.size());
        assertThat(apiGroups.size(), not(0));
        Collection<TAuthority> authorities = repo.getAuthorities();
        logger.info("authorities size = " + authorities.size());
        assertThat(authorities.size(), not(0));
        Collection<TDataSource> dataSources = repo.getDataSources();
        logger.info("dataSources size = " + dataSources.size());
        assertThat(dataSources.size(), not(0));
        Collection<TUser> users = repo.getUsers();
        logger.info("users size = " + users.size());
        assertThat(users.size(), not(0));
        Collection<TUserDateCount> userDateCounts = repo.getUserDateCounts();
        logger.info("userDateCounts size = " + userDateCounts.size());
        assertThat(apis.size(), not(0));
    }

}
