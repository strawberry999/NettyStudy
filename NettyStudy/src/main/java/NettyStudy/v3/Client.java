package NettyStudy.v3;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class Client {

    public void connect(){
        //线程池
        EventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();

        try{
            ChannelFuture future = bs.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer())
                    .connect("localhost", 8888);

            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("connected...");
                    }else{
                        System.out.println("not connected...");
                    }
                }
            });

            future.sync();//等待连接结果返回

            future.channel().closeFuture().sync();//等待通道返回后再往下执行

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }


    public static void main(String[] args) {

        Client client = new Client();
        client.connect();
    }
}

class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ClientHandler());
    }


}

class ClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * ctx中聚合了一个channel
     * ctx是channel的上下文环境
     * ctx.writeAndFlush()方法会自动关闭channel
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //channel 第一次连上可用时，往回写一个hello字符串
        ByteBuf buf = Unpooled.copiedBuffer("hello".getBytes());
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = null;
        try{
            buf = (ByteBuf)msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(),bytes);
            System.out.println(new String(bytes));
            System.out.println(buf.refCnt());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(buf != null){
                ReferenceCountUtil.release(buf);
                System.out.println(buf.refCnt());
            }
        }
    }
}