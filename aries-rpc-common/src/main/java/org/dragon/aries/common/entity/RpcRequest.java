package org.dragon.aries.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * 消费者向提供者发送的请求对象
 *
 * @author ziyang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * 请求号
     */
    private String requestId;
    /**
     * 待调用接口名称
     */
    private String interfaceName;
    /**
     * 待调用方法名称
     */
    private String methodName;
    /**
     * 调用方法的参数
     */
    private Object[] parameters;
    /**
     * 调用方法的参数类型
     */
    private Class<?>[] paramTypes;
    /**
     * 版本号
     */
    private String version;

    /**
     * 是否是心跳包
     */
    private Boolean heartBeat;

    public static RpcRequest heart() {
        return new RpcRequest(UUID.randomUUID().toString(), null, null, new Object[0], new Class[0], null, true);
    }

    public static String randomRequestId() {
        return UUID.randomUUID().toString();
    }

}
