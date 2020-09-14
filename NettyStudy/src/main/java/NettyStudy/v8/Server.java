package NettyStudy.v8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.StringUtil;

public class Server {

    public static ChannelGroup client = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void main(String[] args) {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(2);

        ServerBootstrap sbs = new ServerBootstrap();

        try{
            ChannelFuture future = sbs.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new ServerChildHandler());
                        }
                    })
                    .bind(8888)
                    .sync();

            System.out.println("server started...");

            future.channel().closeFuture().sync();//close-->ChannelFuture

            System.out.println("server ended...");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}

class ServerChildHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server.client.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       ByteBuf buf = null;
       try{
           buf = (ByteBuf)msg;
           byte[] bytes = new byte[buf.readableBytes()];
           buf.getBytes(buf.readerIndex(),bytes);
           String accepted = new String(bytes);
           System.out.println(accepted);

           if(!StringUtil.isNullOrEmpty(accepted) && accepted.equals("_byte_")){
               System.out.println("client ask to exit...");
               //客户端请求退出，移除channel并关闭ctx
               Server.client.remove(ctx.channel());
               ctx.close();
           }else{
               Server.client.writeAndFlush(msg);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }finally {
           //if(buf != null && buf) ReferenceCountUtil.release(buf);
       }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //移除异常的客户端channel，并关闭连接
        Server.client.remove(ctx.channel());
        ctx.close();
    }
}