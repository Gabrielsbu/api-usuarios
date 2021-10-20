package com.dextra.makemagic.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {
    private Integer httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
}
