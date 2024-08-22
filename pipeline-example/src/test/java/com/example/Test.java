package com.example;

import com.example.dto.IdleResult;
import com.example.service.IdleIMLimitWriteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Test {
    @Autowired
    private IdleIMLimitWriteService idleIMLimitWriteService;


    @org.junit.Test
    public void test() {
        log.info("test start->>>>>>>>>>>>>>>>>>>>>");
        idleIMLimitWriteService.consumeQuota("IML", 2L, 2L,
                "messageId", 1L, 1L);
    }
}
