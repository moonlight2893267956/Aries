package org.dragon.aries.common.entity.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcRegisterInterface {
    // 注册接口名称
    private String interfaceName;
    // 注册接口版本
    private String version;
    // 注册实例
    private Object instance;
}
