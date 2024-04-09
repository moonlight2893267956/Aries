package org.dragon.aries.core.protocal.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dragon.aries.common.exception.RpcException;
import org.dragon.aries.core.protocal.RpcStarter;
import org.dragon.aries.core.protocal.netty.codec.CommonDecoder;
import org.dragon.aries.core.protocal.netty.codec.CommonEncoder;
import org.dragon.aries.core.protocal.netty.handler.RpcMessageClientHandler;
import org.dragon.aries.core.serialize.CommonSerializer;

import java.net.InetSocketAddress;

public class NettyRpcClient extends RpcStarter {
    private final static Logger log = LogManager.getLogger(NettyRpcClient.class);
    private NioEventLoopGroup workGroup;
    private Bootstrap bootstrap;
    private final CommonSerializer serializer;

    public NettyRpcClient(CommonSerializer serializer) {
        this.bootstrap = new Bootstrap();
        this.workGroup = new NioEventLoopGroup();
        this.serializer = serializer;
    }

    @Override
    public void start(String host, int port) {
        Channel channel;
        try {
            channel = bootstrap.group(this.workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new RpcMessageClientHandler());
                        }
                    }).connect(new InetSocketAddress(host, port))
                    .sync().channel();
        } catch (InterruptedException e) {
            throw new RpcException("[NettyRpcClient]服务端连接失败");
        }
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.warn("[NettyRpcClient]客户端连接关闭");
                workGroup.shutdownGracefully();
            }
        });
    }
}
