package org.dragon.aries.core.register;

import org.dragon.aries.common.config.ZookeeperConfiguration;
import org.dragon.aries.common.entity.bo.RpcRegisterInterface;
import org.dragon.aries.common.exception.RegisterException;
import org.dragon.aries.common.fatory.SingletonFactory;

public class ZookeeperServiceRegister implements RpcRegister<RpcRegisterInterface> {
    private ZookeeperConfiguration configuration;
    public ZookeeperServiceRegister() throws RegisterException {
        this.configuration = SingletonFactory.getInstance(ZookeeperConfiguration.class);
        this.configuration.config();
    }
    @Override
    public void register(RpcRegisterInterface data) {

    }

    @Override
    public Object getInstance(RpcRegisterInterface data) {
        return null;
    }

    @Override
    public void start() {

    }
}
