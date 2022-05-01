package edu.dlu.bysj.base.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author XiangXinGang
 * @date 2021/10/10 20:57
 */
@Configuration
public class RedisConfig {
  private final RedisConnectionFactory redisConnectionFactory;

  @Autowired
  public RedisConfig(RedisConnectionFactory redisConnectionFactory) {
    this.redisConnectionFactory = redisConnectionFactory;
  }

  /**
   * RedisTemplate 配置
   *
   * @return redisTemplate
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    // TODO 设置redis 连接工厂
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    // TODO 指定key 序列化策略为String 序列哈，value 为jdk 自带序列化
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer(Object.class);

    // 解决查询缓存转换异常的问题
    ObjectMapper om = new ObjectMapper();
    // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);

    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    return redisTemplate;
  }

  @Bean
  public StringRedisTemplate stringRedisTemplate() {
    // TODO 采用默认配置
    StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
    stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
    return stringRedisTemplate;
  }
}
