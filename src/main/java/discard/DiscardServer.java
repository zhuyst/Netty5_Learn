package discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Discard服务端
 */
public class DiscardServer {

    /**
     * 服务端开启端口
     */
    private int port;

    public DiscardServer(int port){
        this.port = port;
    }

    public void run(){

        // 服务端需要两个Group
        // 第一个为Boss，负责接收连接
        EventLoopGroup boosGroup = new NioEventLoopGroup();

        // 第二个为Worker，负责处理Boss接收的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 服务端专用Bootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boosGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)

                    // 通过添加Handler处理接收的连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    .addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            // 启动服务端
            ChannelFuture future = bootstrap.bind(port).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException ignored) {
        } finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        new DiscardServer(port).run();
    }
}
