package com.example.service;

import com.example.dto.ExgetResult;
import com.example.dto.ExsetParams;

/**
 * 用于实现幂等
 */
public interface TairStringInterface {
    ExgetResult<String> exget(String key);

    String exset(String key, String number, ExsetParams params);
}
