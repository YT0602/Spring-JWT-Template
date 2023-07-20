package jwt.oauth.login.global.utils.sms.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class SmsResponseDto {
    private String requestId;
    private LocalDateTime requestTime;
    private String statusCode;
    private String statusName;
    private String smsConfirmNum;


}
