package com.exp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.exp.dto.BookAssess;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagListService {

    private static final String API_KEY = "sk-k3uilZk91WC2tabC0ITbvUlQMaaA4zl65cpgxa9Hqcp2eiKh";
    private static final String API_URL = "https://api.openai-proxy.org/v1/chat/completions";

    public List<String> tagList(String book, String assess) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost request = new HttpPost(API_URL);
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);

            JSONObject payload = new JSONObject();
            payload.put("model", "gpt-3.5-turbo");

            List<JSONObject> messages = new ArrayList<>();
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是一个擅长从评论中提取关键词的专家。");
            messages.add(systemMessage);

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", String.format("请提取下面有关书目《%s》的评论中的关键词，要求给出若干行（不超过3行），每行一个关键词。每个关键词（例如：“勇气”，“治愈”，“爱与和平”）长度不超过4个汉字:\n%s", book, assess));
            messages.add(userMessage);

            payload.put("messages", messages);
            payload.put("temperature", 0);

            request.setEntity(new StringEntity(payload.toString(), StandardCharsets.UTF_8));


            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);
                JSONObject responseObject = JSON.parseObject(responseString);
                String content = responseObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");

                String[] extractedKeywords = content.split("\n");
                List<String> keywords = new ArrayList<>();
                for (String keyword : extractedKeywords) {
                    keywords.add(keyword.trim());
                }

                return keywords;
            }
        } finally {
            httpClient.close();
        }
    }

    public List<String> getTagList(BookAssess bookAssess) {
        // Set the proxy if needed
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "7890");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");

        List<String> tagList = new ArrayList<>();

        try {
            List<String> keywords = new TagListService().tagList(bookAssess.getBook(), bookAssess.getAssess());
            tagList.addAll(keywords);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tagList;
    }

    public List<String> getTagList(List<BookAssess> bookAssessList) {
        // Set the proxy if needed
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "7890");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");

        List<String> tagList = new ArrayList<>();

        for (BookAssess bookAssess : bookAssessList) {
            try {
                List<String> keywords = new TagListService().tagList(bookAssess.getBook(), bookAssess.getAssess());
                tagList.addAll(keywords);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return tagList;
    }

    public static void main(String[] args) {
        // Set the proxy if needed
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "7890");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");


        TagListService tagListService = new TagListService();
        try {
            List<String> keywords = tagListService.tagList("百年孤独", "每个人都是孤独地出生，在这世间恍惚几十年并不漫长的日子转眼就远去了，然后再孤独地死去。 生命注定是个悲剧，因为我们从没有融入世界，世 \n" +
                    "界永远是身外之物。如果有幸，能在茫茫人海寻得一个身体与灵魂都与自己万分契合的人，与之存在一种可以称之为爱情的联系，然后一起承受生命中不可逃离不可消除的深沉的宿命的孤。可是这般的幸运艰深难得。有的已失去了爱的能力，有的爱得深沉却无处安放，有的死在这爱里……在所有的爱里，孤独有增无减。 生命只是一场幻梦。");
            System.out.println(keywords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
