package discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * Discard服务端处理
 */
public class DiscardServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接成功");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接断开");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf in = (ByteBuf) msg;

            // 一个一个byte地读取字符串
            // 客户端发送的是转为byte流的字符串，直接转换即可
            while(in.isReadable()){
                char c = (char) in.readByte();
                System.out.print(c);
            }
            System.out.println();

            // 也可以通过这种方式读取客户端发送的字符串
//            String s = in.toString(CharsetUtil.UTF_8);
//            System.out.println(s);
        } finally {

            // 释放内存
            ReferenceCountUtil.release(msg);
        }
    }
}
