package org.dragon.aries.core.discovery.service;

import lombok.Data;
import org.dragon.aries.common.entity.Socket;
import org.dragon.aries.core.discovery.Discovery;
@Data
public abstract class ServiceDiscover implements Discovery {
    private Socket socket;
}
