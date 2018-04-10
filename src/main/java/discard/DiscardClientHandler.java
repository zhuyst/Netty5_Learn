package discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.util.Scanner;

/**
 * Discard客户端处理
 */
public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 读取输入工具
     */
    private Scanner scanner;

    /**
     * 通道上下文
     */
    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接成功");

        // 通道启动，初始化scanner，设置上下文到成员变量
        this.scanner = new Scanner(System.in);
        this.ctx = ctx;

        // 读取输入信息
        readMsg();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接断开");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    private void readMsg() throws IOException {

        // 读取输入信息
        String msg = scanner.nextLine();

        // 在直接内存创建对象，把输入放入直接内存当中
        ByteBuf buf = ctx.alloc().directBuffer().writeBytes(msg.getBytes());

        // 发送信息并且监听发送情况
        ctx.writeAndFlush(buf).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {

                // 如果发送成功，继续读取输入
                if(future.isSuccess()){
                    readMsg();
                }
                else {
                    future.cause().printStackTrace();
                    future.channel().close();
                }
            }
        });
    }
}
