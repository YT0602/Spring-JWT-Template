package jwt.oauth.login.global.utils.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jwt.oauth.login.global.utils.sms.dto.MessageDto;
import jwt.oauth.login.global.utils.sms.dto.SmsRequestDto;
import jwt.oauth.login.global.utils.sms.dto.SmsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String phone;

    public String getSignature(String time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException{
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;
        log.info(this.accessKey);
        log.info(this.secretKey);
        log.info(this.serviceId);


        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(time)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    public SmsResponseDto sendSms(MessageDto messageDto) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
        String time = Long.toString(System.currentTimeMillis());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", getSignature(time)); // signature 서명

        List<MessageDto> messages = new ArrayList<>();
        messages.add(messageDto);

        String smsConfirmNum = sendRandomMessage();
        SmsRequestDto smsRequest = SmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(phone)
                .content("[운이좋아] 인증번호" + "\n" + "["  + smsConfirmNum + "]를 입력해주세요")
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(smsRequest);

        // jsonBody와 헤더 조립
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        //restTemplate로 post 요청 보내고 오류가 없으면 202코드 반환
        SmsResponseDto smsResponseDto = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, SmsResponseDto.class);
        smsResponseDto.setSmsConfirmNum(smsConfirmNum);
        // redisUtil.setDataExpire(smsConfirmNum, messageDto.getTo(), 60 * 3L); // 유효시간 3분
        return smsResponseDto;
    }

    public String sendRandomMessage() {
        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        return numStr;
    }
}
