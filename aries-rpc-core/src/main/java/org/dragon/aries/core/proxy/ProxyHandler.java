package org.dragon.aries.core.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.common.entity.RpcResponse;
import org.dragon.aries.common.entity.Socket;
import org.dragon.aries.common.entity.bo.CommonField;
import org.dragon.aries.common.enumeration.ResponseCode;
import org.dragon.aries.common.exception.RpcException;
import org.dragon.aries.common.fatory.SingletonFactory;
import org.dragon.aries.common.lb.RobinLoadBalance;
import org.dragon.aries.core.discovery.service.ZookeeperServiceDiscover;
import org.dragon.aries.core.protocal.RpcStarter;
import org.dragon.aries.core.protocal.netty.client.NettyRpcClient;
import org.dragon.aries.core.serialize.JsonSerializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {
    private final static Logger log = LogManager.getLogger(ProxyHandler.class);
    private final CommonField field;

    public ProxyHandler(CommonField field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            String interfaceName = method.getDeclaringClass().getName();
            int retry = field.getRetry();
            long retryInterval = field.getRetryInterval();
            long timeout = field.getTimeout();
            String callback = field.getCallback();
            ZookeeperServiceDiscover discover = new ZookeeperServiceDiscover(new RobinLoadBalance());
            discover.discovery(interfaceName, field.getVersion());
            Socket socket = discover.getSocket();

            RpcStarter rpcStarter = new NettyRpcClient(new JsonSerializer());
            rpcStarter.start(socket);

            Class<?>[] paramTypes = method.getParameterTypes();
            String methodName = method.getName();
            RpcRequest rpcRequest = new RpcRequest(RpcRequest.randomRequestId(), interfaceName, methodName, args, paramTypes, field.getVersion(), false);

            RpcResponse<?> response = rpcStarter.send(rpcRequest, timeout);
            while (response == null || response.getStatusCode().equals(ResponseCode.FAIL.getCode())) {
                log.error("[ProxyHandler] remote service handler fail, having retry count {}", retry);
                if (retry == 0 && StringUtil.isNullOrEmpty(callback)) {
                    throw new RpcException("[ProxyHandler] remote service handler fail");
                } else if (retry == 0) {
                    return callback;
                }
                retry --;
                Thread.sleep(retryInterval);
                response = rpcStarter.send(rpcRequest, timeout);
            }

            if (method.getReturnType().equals(String.class)) {
                return response.getData().toString().replaceAll("^\"|\"$", "");
            }
            ObjectMapper mapper = SingletonFactory.getInstance(ObjectMapper.class);
            return mapper.readValue(response.getData().toString(), method.getReturnType());
        } catch (Exception e) {
            throw new RpcException("[ProxyHandler] remote service handler fail, message " + e.getMessage());
        }
    }
}
