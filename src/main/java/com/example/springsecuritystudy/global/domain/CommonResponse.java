package com.example.springsecuritystudy.global.domain;

import com.example.springsecuritystudy.global.domain.enums.ResultCode;
import lombok.*;

@Data
@NoArgsConstructor
public class CommonResponse<T> {
    private int code;
    private String msg;

    public static<T> CommonResponse<T> success(){
        return CommonResponse.<T>builder()
                .code(ResultCode.OK.getCode())
                .msg(ResultCode.OK.getMessage())
                .build();
    }

    public static<T> CommonResponse<T> fail(ResultCode result){
        return CommonResponse.<T>builder()
                .code(result.getCode())
                .msg(result.getMessage())
                .build();
    }
    @Builder
    public CommonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
