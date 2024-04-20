package org.dragon.aries.common.lb;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance{
    private final Random random;
    public RandomLoadBalance() {
        random = new Random();
    }
    @Override
    public String select(List<String> sockets) {
        if (sockets == null || sockets.isEmpty()) {
            return null;
        }
        int index = random.nextInt(sockets.size());
        return sockets.get(index);
    }
}
