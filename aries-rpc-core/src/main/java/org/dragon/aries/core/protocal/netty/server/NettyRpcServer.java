package org.dragon.aries.core.protocal.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.dragon.aries.common.entity.RpcRequest;
import org.dragon.aries.core.protocal.RpcServer;
import org.dragon.aries.core.protocal.netty.codec.CommonDecoder;
import org.dragon.aries.core.protocal.netty.codec.CommonEncoder;
import org.dragon.aries.core.serialize.CommonSerializer;

public class NettyRpcServer extends RpcServer {
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private CommonSerializer serializable;
    public NettyRpcServer(CommonSerializer commonSerializer) {
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup(2);
        this.serializable = commonSerializer;
    }

    @Override
    public void start(String host, int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.SO_BACKLOG, 256)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new CommonEncoder(serializable));
                        pipeline.addLast(new CommonDecoder());
                        pipeline.addLast(new SimpleChannelInboundHandler() {

                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
                                if (object instanceof RpcRequest) {
                                    RpcRequest request = (RpcRequest) object;
                                    if (request.getHeartBeat()) {

                                    }
                                }
                            }
                        })
                    }
                })
    }
}
