//package com.yongy.dotori.global.redis;
//
//import jakarta.annotation.PostConstruct;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//
//@Service
//public class RedisUtil {
//
//    @Autowired
//    private StringRedisTemplate redisTemplate = redisTemplate = new StringRedisTemplate();
//
////    @PostConstruct
////    private void init(){
////        redisTemplate = new StringRedisTemplate();
////    }
//
//    // key를 통해 value를 반환한다.
//    public String getData(String key){
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        return valueOperations.get(key);
//    }
//
//    public void setData(String key, String value){
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set(key, value);
//    }
//
//    // 유효 시간 동안 (key, value) 저장
//    public void setDataExpire(String key, String value, long duration){
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        Duration expireDuration = Duration.ofSeconds(duration);
//        valueOperations.set(key, value, expireDuration);
//    }
//
//    // 삭제
//    public void deleteData(String key){
//        redisTemplate.delete(key);
//    }
//}
