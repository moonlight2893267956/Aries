package org.dragon.aries.common.entity.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonField {
    private String version;
    private int retry;
    private Long retryInterval;
    private Long timeout;
    private String callback;
}
