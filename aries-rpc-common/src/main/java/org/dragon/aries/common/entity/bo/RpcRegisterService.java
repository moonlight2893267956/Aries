package org.dragon.aries.common.entity.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcRegisterService {
    private String serviceName;
    private String version;
}
