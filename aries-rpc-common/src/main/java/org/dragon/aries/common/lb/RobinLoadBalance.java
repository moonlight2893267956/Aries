package org.dragon.aries.common.lb;

import java.util.List;

public class RobinLoadBalance implements LoadBalance {
    private final int index;
    public RobinLoadBalance() {
        this.index = 0;
    }
    @Override
    public String select(List<String> sockets) {
        return sockets.get((index + 1) % sockets.size());
    }
}
