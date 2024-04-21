package org.dragon.aries.core.protocal.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.common.entity.RpcResponse;
import org.dragon.aries.core.protocal.RpcStarter;
import org.dragon.aries.core.protocal.netty.codec.CommonDecoder;
import org.dragon.aries.core.protocal.netty.codec.CommonEncoder;
import org.dragon.aries.core.protocal.netty.handler.RpcMessageServerHandler;
import org.dragon.aries.core.serialize.CommonSerializer;

import java.net.InetSocketAddress;

public class NettyRpcServer extends RpcStarter {
    private static final Logger log = LogManager.getLogger(NettyRpcServer.class);
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final ServerBootstrap bootstrap;
    private final CommonSerializer serializable;

    public NettyRpcServer(CommonSerializer commonSerializer) {
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup(2);
        this.serializable = commonSerializer;
        this.bootstrap = new ServerBootstrap();
    }

    @Override
    public Channel start(String host, int port) {
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.SO_BACKLOG, 256)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new IdleStateHandler(5, 0, 0));
                        pipeline.addLast(new CommonEncoder(serializable));
                        pipeline.addLast(new CommonDecoder());
                        pipeline.addLast(new RpcMessageServerHandler());
                    }
                });
        ChannelFuture bind = bootstrap.bind(new InetSocketAddress(host, port));
        log.info("[NettyRpcServer] Server started at {}:{}", host, port);
        bind.channel().closeFuture().addListener((ChannelFutureListener) channelFuture -> {
            log.warn("[NettyRpcServer] Server stopped");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        });
        return bind.channel();
    }

    @Override
    public RpcResponse<?> send(RpcRequest request, Long timeout) {
        return null;
    }
}
