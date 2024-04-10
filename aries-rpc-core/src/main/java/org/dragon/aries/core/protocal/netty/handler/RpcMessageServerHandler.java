package org.dragon.aries.core.protocal.netty.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.common.entity.RpcResponse;
import org.dragon.aries.common.fatory.SingletonFactory;

public class RpcMessageServerHandler extends ChannelInboundHandlerAdapter {
    private final static Logger log = LogManager.getLogger(RpcMessageServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) msg;

            // 如果服务端接受心跳包则回写一个心跳包给客户端
            if (request.getHeartBeat()) {
//                log.info("心跳包接收成功，当前时刻：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
                ctx.writeAndFlush(RpcResponse.heart(request.getRequestId()));
                return;
            }
            log.info("接受数据包：{}", request);
            RpcMethodHandler handler = SingletonFactory.getInstance(RpcMethodHandler.class);
            ObjectMapper mapper = SingletonFactory.getInstance(ObjectMapper.class);
            Object invoke = null;
            try {
                invoke = handler.handleMethod(request.getInterfaceName(), request.getMethodName(), request.getParameters(), request.getParamTypes(), request.getVersion());
            } catch (RuntimeException e) {
                log.info("[RpcMessageServerHandler]处理失败，失败原因：{}", e.toString());
            }

            /**
             * 1. 方法调度
             * 2. 方法调用结果回写
             */
            ctx.writeAndFlush(RpcResponse.success(mapper.writeValueAsString(invoke), request.getRequestId()));
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state().equals(IdleState.READER_IDLE)) {
            log.warn("服务端长时间未接受心跳包，连接断开");
            ctx.channel().close().sync();
        }
    }
}
