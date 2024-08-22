package com.example.service.impl;

import com.example.dto.ExgetResult;
import com.example.dto.ExsetParams;
import com.example.service.TairStringInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TairStringInterfaceImpl implements TairStringInterface {

    @Autowired
    private RedisTemplate redisTemplate;
    public ExgetResult<String> exget(String key) {
        String value = (String) redisTemplate.opsForValue().get(key);
        return new ExgetResult<>(value);
    }

    public String exset(String key, String number, ExsetParams params) {
        Boolean b = redisTemplate.opsForValue().setIfAbsent(key, number, params.getEx(), TimeUnit.SECONDS);
        return b ? "OK" : "fail";
    }
}
