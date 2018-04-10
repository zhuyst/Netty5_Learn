package discard;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Discard客户端
 */
public class DiscardClient {

    /**
     * 服务端主机地址
     */
    private String host;

    /**
     * 服务端端口
     */
    private int serverPort;

    public DiscardClient(String host,int serverPort){
        this.host = host;
        this.serverPort = serverPort;
    }

    public void run(){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 客户端使用的Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new DiscardClientHandler());

            // 连接服务端
            ChannelFuture future = bootstrap.connect(host,serverPort).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException ignored) {
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 8080;
        new DiscardClient(host,port).run();
    }
}
