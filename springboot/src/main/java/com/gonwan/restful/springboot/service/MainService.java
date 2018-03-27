package com.gonwan.restful.springboot.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gonwan.restful.springboot.Config;
import com.gonwan.restful.springboot.DatabaseClient;
import com.gonwan.restful.springboot.PubSubChannel;
import com.gonwan.restful.springboot.RepoReloadFlag;
import com.gonwan.restful.springboot.RestfulException;
import com.gonwan.restful.springboot.RestfulException.Predefined;
import com.gonwan.restful.springboot.RestfulRepository;
import com.gonwan.restful.springboot.SqlGenerator;
import com.gonwan.restful.springboot.model.TApi;
import com.gonwan.restful.springboot.model.TAuthority;
import com.gonwan.restful.springboot.model.TDataSource;
import com.gonwan.restful.springboot.model.TUser;
import com.gonwan.restful.springboot.request.GetRowCountRequest;
import com.gonwan.restful.springboot.request.ReloadRequest;
import com.gonwan.restful.springboot.request.RunApiRequest;

@Component
class RowCountTask {

    private static final Logger logger = LoggerFactory.getLogger(RowCountTask.class);

    @Autowired
    private Config config;
    @Autowired
    private RestfulRepository repo;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private Map<String, Long> userCountMap = new HashMap<>();
    private LocalDate prevDate = LocalDate.now();

    synchronized public long add(String username, long delta) {
        Long count = userCountMap.get(username);
        if (count == null) {
            count = delta;
        } else {
            count += delta;
        }
        userCountMap.put(username, count);
        return count;
    }

    @Scheduled(initialDelay = 30*1000, fixedRate = 30*1000)
    synchronized public void run() {
        boolean needUpdate = false;
        for (Map.Entry<String, Long> e : userCountMap.entrySet()) {
            String username = e.getKey();
            Long rowCount = e.getValue();
            if (rowCount != 0) {
                logger.info("RowCount to be updated: {}={}", username, rowCount);
                repo.updateUserCountToday(username, rowCount);
                userCountMap.put(username, 0L);
                needUpdate = true;
            }
        }
        LocalDate currDate = LocalDate.now();
        if (currDate.getDayOfMonth() != prevDate.getDayOfMonth()) {
            prevDate = currDate;
            userCountMap.clear();
            needUpdate = true;
        }
        if (needUpdate) {
            redisTemplate.convertAndSend(config.getRedisPublishPrefix() + PubSubChannel.REPO_RELOAD.toString(), RepoReloadFlag.USER_DATA_COUNT.toString());
        }
    }

}

@Service
public class MainService {

    private static final Logger logger = LoggerFactory.getLogger(MainService.class);

    @Autowired
    private Config config;
    @Autowired
    private RestfulRepository repo;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;
    @Autowired
    private DatabaseClient dbClient;
    @Autowired
    private ApiService apiService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private RowCountTask rowCountTask;

    @PostConstruct
    public void init() {
        /* pubsub */
        redisMessageListenerContainer.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String channel = new String(message.getChannel());
                String body = new String(message.getBody());
                logger.info("Message received: {}={}", channel, body);
                String ch = channel.substring(config.getRedisPublishPrefix().length());
                if (ch.equals(PubSubChannel.REPO_RELOAD.toString())) {
                    RepoReloadFlag flags = RepoReloadFlag.parseString(body);
                    repo.reload(flags);
                }
            }
        }, new PatternTopic(config.getRedisPublishPrefix() + "*"));
    }

    public void reload(ReloadRequest request) throws RestfulException {
        redisTemplate.convertAndSend(config.getRedisPublishPrefix() + PubSubChannel.REPO_RELOAD.toString(), request.getDataType());
    }

    public long getRowCount(GetRowCountRequest request) throws RestfulException {
        String user = request.getUsername();
        String password = request.getPassword();
        /* check */
        loginService.checkAuthentication(user, password);
        checkDate(request.getStartDate(), request.getEndDate());
        /* get api */
        TApi api = apiService.getApi(request);
        /* run sql */
        TDataSource source = getDataSource(api.getDatasourceId(), api.getDatasourceDatabaseId());
        String sql = SqlGenerator.get(request, api, source.getDialect());
        logger.info("Executing SQL: {}", sql);
        long t0 = System.currentTimeMillis();
        long res = dbClient.getRowCount(source.getConnectionString(), source.getReadUsername(), source.getReadPassword(), sql);
        long elapsed = System.currentTimeMillis() - t0;
        logger.info("Executed SQL in {} ms", elapsed);
        return res;
    }

    /* return a list or stream */
    public Object runApi(RunApiRequest request) throws RestfulException {
        String username = request.getUsername();
        String password = request.getPassword();
        /* check */
        loginService.checkAuthentication(username, password);
        checkDate(request.getStartDate(), request.getEndDate());
        /* get api */
        TApi api = apiService.getApi(request);
        /* check auth */
        TAuthority auth = verifyAndGetAuthorities(request, api);
        /* check quota */
        checkQuota(request, auth);
        /* run sql */
        TDataSource source = getDataSource(api.getDatasourceId(), api.getDatasourceDatabaseId());
        String sql = SqlGenerator.get(request, api, source.getDialect());
        logger.info("Executing SQL: {}", sql);
        long t0 = System.currentTimeMillis();
        boolean streamContent = request.getPageSize() > config.getContentStreamThreshold();
        Object res = dbClient.runApi(source.getConnectionString(), source.getReadUsername(), source.getReadPassword(), sql, streamContent);
        long currentRowCount = request.getPageSize();
        long elapsed = System.currentTimeMillis() - t0;
        logger.info("Executed SQL in {} ms", elapsed);
        rowCountTask.add(username, currentRowCount);
        return res;
    }

    private TAuthority verifyAndGetAuthorities(GetRowCountRequest request, TApi api) throws RestfulException {
        TUser user = repo.getUsers().stream()
                .filter(x -> StringUtils.equals(x.getUsername(), request.getUsername()))
                .findFirst().orElse(null);
        if (user == null) {
            throw Predefined.USER_NOT_EXIST;
        }
        TAuthority auth = repo.getAuthorities().stream()
                .filter(x -> Objects.equals(x.getUserId(), user.getId()))
                .findFirst().orElse(null);
        if (auth == null) {
            throw Predefined.USER_NOT_AUTHORIZED;
        }
        List<String> userGroups = Arrays.asList(StringUtils.split(StringUtils.trimToEmpty(auth.getReadApiGroupIds()), ",")).stream()
                .map(x -> StringUtils.trimToEmpty(x))
                .collect(Collectors.toList());
        List<String> apiGroups = Arrays.asList(StringUtils.split(StringUtils.trimToEmpty(api.getApiGroups()), ",")).stream()
                .map(x -> StringUtils.trimToEmpty(x))
                .collect(Collectors.toList());
        userGroups.retainAll(apiGroups); /* intersect */
        if (userGroups.size() <= 0) {
            throw Predefined.USER_API_NOT_AUTHORIZED;
        }
        return auth;
    }

    private TDataSource getDataSource(Long dataSourceId, Long dataSourceDatabaseId) throws RestfulException {
        List<TDataSource> dataSources = repo.getDataSources().stream()
                .filter(x -> Objects.equals(x.getId(), dataSourceId)
                        && Objects.equals(x.getDatabaseId(), dataSourceDatabaseId))
                .collect(Collectors.toList());
        if (dataSources.size() == 0) {
            throw Predefined.SYS_NO_CONNECTION_STRING;
        }
        if (dataSources.size() > 1) {
            throw Predefined.SYS_MULTIPLE_CONNECTION_STRING;
        }
        TDataSource dataSource = dataSources.get(0);
        if (StringUtils.isAnyEmpty(dataSource.getReadUsername(), dataSource.getReadPassword(), dataSource.getConnectionString())) {
            throw Predefined.SYS_INVALID_CONNECTION_STRING;
        }
        return dataSource;
    }

    private void checkDate(LocalDate startDate, LocalDate endDate) throws RestfulException {
        if (startDate != null && endDate != null && startDate.compareTo(endDate) > 0) {
            throw Predefined.REQ_DATE_RANGE_ERROR;
        }
    }

    public void checkQuota(GetRowCountRequest request, TAuthority auth) throws RestfulException {
        if (request instanceof RunApiRequest && ((RunApiRequest)request).getPageSize() > auth.getMaxRowOnce()) {
            throw Predefined.USER_EXCEED_MAX_QUERY_QUOTA;
        }
        Long oldRowCount = repo.getUserDateCounts().stream()
                .filter(x -> StringUtils.equals(x.getUsername(), request.getUsername()))
                .map(x -> x.getRowCount())
                .findFirst().orElse(0L);
        if (oldRowCount > auth.getMaxRowDaily()) {
            throw Predefined.USER_EXCEED_MAX_DAILY_QUOTA;
        }
    }

}
