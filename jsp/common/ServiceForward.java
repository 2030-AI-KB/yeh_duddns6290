package com.common;

import lombok.Builder;
import lombok.Data;

// 경로 여부를 결정하는 클래스
@Data
@Builder
public class ServiceForward {
    private  String path;
    // true일때 redirect, false일때 forward
    private boolean redirect;
}
