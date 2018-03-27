package com.gonwan.restful.springboot;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class DatabaseClient {

    @Autowired
    private HikariConfig hikariConfig;

    private Map<String, SessionFactory> connectionMap = new ConcurrentHashMap<>();

    /* hibernate to return java 8 streams. */
    public SessionFactory getDataSource(String connectionString, String username, String password) {
        SessionFactory sessionFactory = connectionMap.get(connectionString);
        if (sessionFactory == null) {
            synchronized (this) {
                sessionFactory = connectionMap.get(connectionString);
                if (sessionFactory == null) {
                    HikariConfig config = new HikariConfig();
                    config.setJdbcUrl(connectionString);
                    config.setUsername(username);
                    config.setPassword(password);
                    config.setMinimumIdle(hikariConfig.getMinimumIdle());
                    config.setMaximumPoolSize(hikariConfig.getMaximumPoolSize());
                    config.setIdleTimeout(hikariConfig.getIdleTimeout());
                    config.setMaxLifetime(hikariConfig.getMaxLifetime());
                    DataSource dataSource = new HikariDataSource(config);
                    Configuration configuration = new Configuration();
                    sessionFactory = configuration.buildSessionFactory(
                            new StandardServiceRegistryBuilder()
                                    .applySettings(configuration.getProperties())
                                    .applySetting(Environment.DATASOURCE, dataSource)
                                    .applySetting(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread")
                                    .build());
                    connectionMap.put(connectionString, sessionFactory);
                }
            }
        }
        return sessionFactory;
    }

    public long getRowCount(String connectionString, String username, String password, String sql) throws RestfulException {
        try (StatelessSession session = getDataSource(connectionString, username, password).openStatelessSession()) {
            /* no entity class, since Long is not an entity class. */
            //Query<Long> query = session.createNativeQuery(sql, Long.class);
            Query<?> query = session.createNativeQuery(sql);
            return ((BigInteger)query.getSingleResult()).longValue();
        } catch (HibernateException e) {
            throw e;
        }
    }

    @SuppressWarnings("deprecation")
    public Object runApi(String connectionString, String username, String password, String sql, boolean streamContent) throws RestfulException {
        StatelessSession session = null;
        try {
            session = getDataSource(connectionString, username, password).openStatelessSession();
            StatelessSession s = session;
            /* Jackson already provides StdJdkSerializers, we can use spring configs for formatting directly. */
            Query<?> query = session.createNativeQuery(sql)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            if (streamContent) {
                return query.getResultStream().onClose(() -> {
                    s.close();
                });
            } else {
                return query.getResultList();
            }
        } catch (HibernateException e) {
            if (session != null) {
                session.close();
            }
            throw e;
        }
    }

}
