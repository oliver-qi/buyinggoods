package buyinggoods.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * redis客户端工具
 *
 * @author jib
 * @date 2019/6/17 17:32
 */
@Component
public class RedisUtil<T> {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * add string type
     *
     * @param key
     * @param value
     */
    public void setStr(String key, T value) {
        if (StringUtils.isNotBlank(key) && Objects.nonNull(value)) {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
        }
    }

    /**
     * delete all type
     * string, hash, list, set, zset
     *
     * @param key
     */
    public void del(String key) {
        if (StringUtils.isNotBlank(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * get string type to object
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getStr(String key, Class<? extends T> tClass) {
        String redValue = redisTemplate.opsForValue().get(key);
        T value = StringUtils.isNotBlank(redValue) ? JSON.parseObject(redValue, tClass) : null;
        return value;
    }

    /**
     * add list type
     *
     * @param key
     * @param valueList
     */
    public void setList(String key, List<T> valueList) {
        if (StringUtils.isNotBlank(key) && !CollectionUtils.isEmpty(valueList)) {
            List<String> stringList = valueList.stream().map(t -> {
                String speech = JSON.toJSONString(t);
                if (StringUtils.isNotBlank(speech)) {
                    redisTemplate.opsForList().rightPush(key, speech);
                }
                return speech;
            })
                    .collect(Collectors.toList());
        }
    }

    /**
     * get list type to List<Object>
     *
     * @param key
     * @param tclass
     * @return
     */
    public List<T> getList(String key, Class<? extends T> tclass) {
        List<String> stringList = redisTemplate.opsForList().range(key, 0, -1);
        List<T> valueList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(stringList)) {
            for (String s : stringList) {
                T value = JSON.parseObject(s, tclass);
                if (Objects.nonNull(value)) {
                    valueList.add(value);
                }
            }
        }
        return valueList;
    }
}
