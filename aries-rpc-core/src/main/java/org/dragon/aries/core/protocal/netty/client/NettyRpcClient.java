package org.dragon.aries.core.protocal.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.common.entity.RpcResponse;
import org.dragon.aries.common.entity.bo.DefaultResponseFuture;
import org.dragon.aries.common.exception.RpcException;
import org.dragon.aries.core.protocal.RpcStarter;
import org.dragon.aries.core.protocal.netty.codec.CommonDecoder;
import org.dragon.aries.core.protocal.netty.codec.CommonEncoder;
import org.dragon.aries.core.protocal.netty.handler.RpcMessageClientHandler;
import org.dragon.aries.core.serialize.CommonSerializer;

import java.net.InetSocketAddress;

public class NettyRpcClient extends RpcStarter {
    private final static Logger log = LogManager.getLogger(NettyRpcClient.class);
    private final NioEventLoopGroup workGroup;
    private final Bootstrap bootstrap;
    private final CommonSerializer serializer;
    private Channel channel;
    private RpcMessageClientHandler handler;

    public NettyRpcClient(CommonSerializer serializer) {
        this.bootstrap = new Bootstrap();
        this.workGroup = new NioEventLoopGroup();
        this.serializer = serializer;
        handler = new RpcMessageClientHandler();
    }

    @Override
    public Channel start(String host, int port) {
        try {
            channel = bootstrap.group(this.workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new IdleStateHandler(5, 3, 0));
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(handler);
                        }
                    }).connect(new InetSocketAddress(host, port))
                    .sync().channel();
        } catch (InterruptedException e) {
            throw new RpcException("[NettyRpcClient]服务端连接失败");
        }
        channel.closeFuture().addListener((ChannelFutureListener) channelFuture -> {
            log.warn("[NettyRpcClient]客户端连接关闭");
            workGroup.shutdownGracefully();
        });
        return channel;
    }

    public RpcResponse<?> send(RpcRequest request) {
        handler.cacheRequest(request.getRequestId(), new DefaultResponseFuture());
        channel.writeAndFlush(request);
        return handler.getResponse(request.getRequestId());
    }
}
