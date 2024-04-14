package org.dragon.aries.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Socket {
    private String host;
    private int port;
}
