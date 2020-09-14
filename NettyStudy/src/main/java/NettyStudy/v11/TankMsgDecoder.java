package NettyStudy.v11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TankMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() < 8){//TCP拆包 粘包的问题，先计算客户端发送的消息的字节数，不够先不处理
            return;
        }

//        in.markReaderIndex();
        int x = in.readInt();
        int y = in.readInt();
        out.add(new TankMsg(x,y));
    }
}
