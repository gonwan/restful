package com.gonwan.restful.springboot.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gonwan.restful.springboot.SpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class)
public class RedisClientTest {

    private static final Logger logger = LoggerFactory.getLogger(RedisClientTest.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        Collection<String> keys = redisTemplate.keys("test*");
        logger.info("Keys: " + keys.toString());
        redisTemplate.delete(keys);
        logger.info("Keys cleaned: " + keys.size());
    }

    @Test
    public void testKeys() throws Exception {
        Collection<String> res = redisTemplate.keys("*");
        assertThat(res.size(), not(0));
    }

    @Test
    public void testSetGet() throws Exception {
        redisTemplate.opsForValue().set("testkey1", "testvalue1");
        String value1 = redisTemplate.opsForValue().get("testkey1");
        assertThat(value1, is("testvalue1"));
        redisTemplate.opsForValue().set("testkey2", "testvalue2", 60, TimeUnit.SECONDS);
        String value2 = redisTemplate.opsForValue().get("testkey2");
        assertThat(value2, is("testvalue2"));
    }

    @Test
    public void testHSetGet() throws Exception {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        ops.put("testkeyh1", "testhash1", "testvalueh1");
        String value1 = ops.get("testkeyh1", "testhash1");
        assertThat(value1, is("testvalueh1"));
    }

    @Test
    public void testMSetGet() throws Exception {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String[] keys1 = new String[] {
            "testkeym1", "testkeym2"
        };
        String[] values1 = new String[] {
            "testvaluem1", "testvaluem2"
        };
        Map<String, String> m1 = IntStream.range(0, 2).boxed().collect(Collectors.toMap(i -> keys1[i], i -> values1[i]));
        ops.multiSet(m1);
        List<String> values12 = ops.multiGet(Arrays.asList(keys1));
        assertThat(values1, is(values12.toArray()));
        /* test large set/get */
        int length = 100;
        String[] keys2 = new String[length];
        String[] values2 = new String[length];
        for (int i = 0; i < length; i++) {
            keys2[i] = "testkeylargem" + i;
            values2[i] = "testvaluelargem" + i;
        }
        Map<String, String> m2 = IntStream.range(0, length).boxed().collect(Collectors.toMap(i -> keys2[i], i -> values2[i]));
        ops.multiSet(m2);
        List<String> values22 = ops.multiGet(Arrays.asList(keys2));
        assertThat(values2, is(values22.toArray()));
    }

    @Test
    public void testHMSetGet() throws Exception {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        String[] keys1 = new String[] {
            "testkeyhm1", "testkeyhm2"
        };
        String[] values1 = new String[] {
            "testvaluehm1", "testvaluehm2"
        };
        Map<String, String> m1 = IntStream.range(0, 2).boxed().collect(Collectors.toMap(i -> keys1[i], i -> values1[i]));
        ops.putAll("testhashm1", m1);
        List<String> values12 = ops.multiGet("testhashm1", Arrays.asList(keys1));
        assertThat(values1, is(values12.toArray()));
        /* test large set/get */
        int length = 100;
        String[] keys2 = new String[length];
        String[] values2 = new String[length];
        for (int i = 0; i < length; i++) {
            keys2[i] = "testkeylargehm" + i;
            values2[i] = "testvaluelargehm" + i;
        }
        Map<String, String> m2 = IntStream.range(0, length).boxed().collect(Collectors.toMap(i -> keys2[i], i -> values2[i]));
        ops.putAll("testhashlargehm", m2);
        List<String> values22 = ops.multiGet("testhashlargehm", Arrays.asList(keys2));
        assertThat(values2, is(values22.toArray()));
    }

    @Test
    public void testNull() throws Exception {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        HashOperations<String, String, String> ops2 = redisTemplate.opsForHash();
        String[] keys = new String[] {
            "testkeynull1", "testkeynull2"
        };
        List<String> values1 = ops.multiGet(Arrays.asList(keys));
        assertThat(values1.get(0), is((String)null));
        assertThat(values1.get(1), is((String)null));
        ops2.putAll("testhashnull", new HashMap<>());
        List<String> values2 = ops2.multiGet("testhashnull", Arrays.asList(keys));
        assertThat(values2.get(0), is((String)null));
        assertThat(values2.get(1), is((String)null));
    }

    @Test
    public void testPubSub() throws Exception {
        /* subscribe */
        redisMessageListenerContainer.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                logger.info(String.format("Got message channel=%s, message=%s", new String(message.getChannel()), message));
            }
        }, new ChannelTopic("testpubsub"));
        redisTemplate.convertAndSend("testpubsub", "testpubsubvalue");
        /* psubscribe */
        redisMessageListenerContainer.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                logger.info(String.format("Got message channel=%s, message=%s", new String(message.getChannel()), message));
            }
        }, new PatternTopic("testpubsub.*"));
        redisTemplate.convertAndSend("testpubsub.1", "testpubsubvalue.1");
        redisTemplate.convertAndSend("testpubsub.2", "testpubsubvalue.2");
        Thread.sleep(3*1000);
    }

}
