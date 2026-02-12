package com.example.controller;

import cn.hutool.core.util.HexUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author suj
 * @create 2025-05-30 09:39
 */
@RestController
@RequestMapping("/work")
@RequiredArgsConstructor
@Slf4j
public class WorkDaliyTaskController {
    // 请求 URL
    private static final String URL = "https://chandao.hityun.cn/zentao/task-batchCreate-14--0.html";

    // 每组任务数量
    private static final int BATCH_SIZE = 10;

    @RequestMapping("/daliyTask")
    public String daliyTask(@RequestBody String tasksJson) {
        List<List<JSONObject>> taskGroup = parseTasks(tasksJson);
        System.out.println(JSONObject.toJSONString(taskGroup));
        for (List<JSONObject> tasks : taskGroup) {
            Map<String, Object> formData = new LinkedHashMap<>();
            int index = 0;
            for (JSONObject task : tasks) {
                formData.put("module[" + index + "]", "0");
                formData.put("parent[" + index + "]", "0");
                formData.put("storyEstimate" + index, "");
                formData.put("storyDesc" + index, "");
                formData.put("storyPri" + index, "");
                formData.put("name[" + index + "]", task.getOrDefault("title", ""));
                formData.put("color[" + index + "]", "");
                formData.put("type[" + index + "]", "devel");
                formData.put("assignedTo[" + index + "]", "xuxiwei");
                formData.put("estimate[" + index + "]", "8");
                formData.put("estStarted[" + index + "]", task.getOrDefault("date", ""));
                formData.put("deadline[" + index + "]", task.getOrDefault("date", ""));
                formData.put("desc[" + index + "]", task.getOrDefault("desc", ""));
                formData.put("pri[" + index + "]", "3");
                index++;
            }
            System.out.println(JSONObject.toJSONString(formData));
            sendBatch(formData, URL);
        }

        return "success";
    }


    private static String generateRandomUid() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);

        return HexUtil.encodeHexStr(bytes)    // 转换为16进制字符串
                .replace("-", "")
                .substring(0, 13);
    }
    /**
     * 发送 HTTP 请求
     */
    private static void sendBatch(Map<String, Object> formData, String url) {
        try {
            HttpResponse response = HttpRequest.post(url)
                    .form(formData)
                    //以下数据自行抓包
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36")
                    .header("Accept", "application/json, text/javascript, */*; q=0.01")
                    .header("Accept-Encoding", "gzip, deflate, br, zstd")
                    .header("pragma", "no-cache")
                    .header("cache-control", "no-cache")
                    .header("sec-ch-ua-platform", "\"Windows\"")
                    .header("x-requested-with", "XMLHttpRequest")
                    .header("sec-ch-ua", "\"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"")
                    .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundarypT4VY6IjwDn2WrrC")
                    .header("sec-ch-ua-mobile", "?0")
                    .header("origin", "https://chandao.hityun.cn")
                    .header("sec-fetch-site", "same-origin")
                    .header("sec-fetch-mode", "cors")
                    .header("sec-fetch-dest", "empty")
                    .header("referer", "https://chandao.hityun.cn/zentao/task-batchCreate-6--.html")
                    .header("accept-language", "zh-CN,zh;q=0.9")
                    .header("xsk", "AHs3bpcY!t7[;DM0E1d6T~8_fj.G5n4O")
                    .header("x-sk", "AHs3bpcY!t7[;DM0E1d6T~8_fj.G5n4O")
                    .cookie("zentaosid=3njb5iikuqg2l4lq0a3vl9kup3; lang=zh-cn; device=desktop; theme=default; keepLogin=on; za=xuxiwei; moduleBrowseParam=0; productBrowseParam=0; lastTaskModule=0; projectTaskOrder=id_desc; pagerMyTask=30; zp=85202962678d3a30c3481babad32e2f2b26a7928; lastProject=14; preProjectID=14; selfClose=0; windowWidth=900; windowHeight=723")
                    .execute();

            System.out.println("Response Code: " + response.getStatus() + ", Body: " + response.body());
        } catch (Exception e) {
            log.info("发送请求异常{}", e.getMessage());
        }
    }

    /**
     * 手动解析 JSON 字符串为 List<Map<String, String>>
     */
    private List<List<JSONObject>> parseTasks(String jsonStr) {
        JSONObject data = JSONObject.parseObject(jsonStr);
        List<JSONObject> result = JSONObject.parseArray(data.getString("result"), JSONObject.class);

        return IntStream.range(0, result.size())
                .boxed()
                .collect(Collectors.groupingBy(
                        index -> index / BATCH_SIZE,  // 分组规则：索引除以批量大小
                        Collectors.mapping(result::get, Collectors.toList())
                ))
                .values()
                .stream()
                .sorted(Comparator.comparingInt(list -> result.indexOf(list.get(0))))
                .collect(Collectors.toList());
    }
}
