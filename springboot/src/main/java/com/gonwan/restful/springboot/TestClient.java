package com.gonwan.restful.springboot;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gonwan.restful.springboot.request.LoginRequest;
import com.gonwan.restful.springboot.request.ResponseBase;
import com.gonwan.restful.springboot.request.RunApiRequest;

/*
 * Test code for reference, do not use.
 */
public class TestClient {

    private static final String SERVER_ADDRESS = "http://192.168.11.196:5050/api";
    private static final String LOGIN_REQUEST = "{ \"username\": \"test2\", \"password\": \"123456\" }";
    private static final String RUNAPI_REQUEST = ""
            + "{ \"apiName\": \"HOLIDAY_INFO\", "
            + "\"apiVersion\": 0, "
            + "\"username\": \"test2\", "
            + "\"password\": \"123456\", "
            + "\"conditions\": null, "
            + "\"startDate\": \"\", "
            + "\"endDate\": \"\", "
            + "\"startPage\": 1, "
            + "\"pageSize\": 3, "
            + "\"columns\": null }";

    private void testRaw() {
        System.out.println("--- testing Raw ---");
        //RestTemplate restTemplate = new RestTemplate();
        /* gzip support */
        HttpClient httpClient = HttpClientBuilder.create().build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String loginResponse = restTemplate.postForObject(
                SERVER_ADDRESS + "/login", new HttpEntity<>(LOGIN_REQUEST, headers), String.class);
        System.out.println(loginResponse);
        String runapiResponse = restTemplate.postForObject(
                SERVER_ADDRESS + "/runapi", new HttpEntity<>(RUNAPI_REQUEST, headers), String.class);
        System.out.println(runapiResponse);
    }

    private void testJSON() throws IOException {
        System.out.println("--- testing JSON ---");
        //RestTemplate restTemplate = new RestTemplate();
        /* gzip support */
        HttpClient httpClient = HttpClientBuilder.create().build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test2");
        loginRequest.setPassword("123456");
        ResponseBase loginResponse = restTemplate.postForObject(
                SERVER_ADDRESS + "/login", new HttpEntity<>(loginRequest, headers), ResponseBase.class);
        System.out.println(String.format("Login: code=%s message=%s", loginResponse.getCode(), loginResponse.getMessage()));
        RunApiRequest runapiRequest = new RunApiRequest();
        runapiRequest.setApiName("HOLIDAY_INFO");
        runapiRequest.setApiVersion(0);
        runapiRequest.setUsername("test2");
        runapiRequest.setPassword("123456");
        runapiRequest.setStartPage(1);
        runapiRequest.setPageSize(3);
        String runapiResponse = restTemplate.postForObject(
                SERVER_ADDRESS + "/runapi", new HttpEntity<>(runapiRequest, headers), String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(runapiResponse);
        System.out.println(String.format("RunApi: code=%s message=%s resultTable=%s",
                root.path("code").asText(), root.path("message").asText(), root.path("resultTable").toString()));
    }

    public static void main(String[] args) throws IOException {
        TestClient client = new TestClient();
        client.testRaw();
        client.testJSON();
    }

}
