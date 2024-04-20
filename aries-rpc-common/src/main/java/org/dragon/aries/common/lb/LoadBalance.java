package org.dragon.aries.common.lb;

import org.dragon.aries.common.entity.Socket;

import java.util.List;

public interface LoadBalance {
    String select(List<String> sockets);
}
