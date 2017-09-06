package com.ice.bike.message;

import java.util.List;

import com.ice.bike.codec.BikeLockMessageInvalidException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class BikeLockDecoder extends ByteToMessageDecoder {
	private static final byte BOUNDARY = 0x7E;
	private static final byte ESCAPE_PRE = 0x5E;
	private static final byte ESCAPE_0x7E = 0x7D;
	private static final byte ESCAPE_0x5E = 0x5D;
	private static final int MIN_MSG_SIZE = 16;
	private static final int MAX_MSG_SIZE = 2048;

	private int offset = 0;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < in.readableBytes(); i++) {
			sb.append(String.format("%02x", in.getByte(i)));
		}
		System.out.println(sb.toString());
		while (offset < in.readableBytes()) {
			byte curByte = in.getByte(offset + in.readerIndex());
			if (offset == 0) {
				if (curByte != BikeLockDecoder.BOUNDARY) {
					System.out.println("BOUNDARY invalid.");
				} else {
					offset++;
				}
			} else {
				if (curByte == BikeLockDecoder.BOUNDARY) {
					int msgLen = offset + 1;
					if (msgLen < MIN_MSG_SIZE) {
						throw new BikeLockMessageInvalidException("Message too short");
					}
					BikeLockMessage msg = BikeLockDecoder.parseMessage(in.readRetainedSlice(msgLen));
					if (msg != null) {
						out.add(msg);
					}
					offset = 0;
				} else {
					if (++offset >= MAX_MSG_SIZE) {
						throw new BikeLockMessageInvalidException("Message too long");
					}
				}
			}
		}
	}

	/** 解析Bytebuf. */
	private static BikeLockMessage parseMessage(ByteBuf byteBuf) throws Exception {
		ByteBuf unwrapped = BikeLockDecoder.unwrap(byteBuf);
		if (false == BikeLockDecoder.CRC(unwrapped)) {
			throw new BikeLockMessageInvalidException("CRC check failed");
		}
		return BikeLockDecoder.unSerialize(unwrapped);
	}

	/** 解析去除掉收尾,和CRC校验之后的ByteBuf. */
	private static BikeLockMessage unSerialize(ByteBuf byteBuf) throws Exception {
		BikeLockMessage message = new BikeLockMessage();
		message.setProtocolType(byteBuf.readByte());
		message.setProtocolVersion(byteBuf.readByte());
		byte[] data = new byte[8];
		for (int i = 7; i >= 0; i--) {
			data[i] = byteBuf.readByte();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			sb.append(String.format("%02d", data[i] & 0xFF));
		}
		message.setDeviceId(sb.toString());
		message.setDeviceType(byteBuf.readByte());
		message.setCommand(byteBuf.readByte());
		message.setReply(byteBuf.readByte());
		message.setReport(new BikeStatusReport(byteBuf));
		return message;
	}

	/** 去掉起始结束标志. */
	private static ByteBuf unwrap(ByteBuf byteBuf) {
		byteBuf.skipBytes(1); // 跳过第一字节
		ByteBuf result = byteBuf.alloc().buffer(byteBuf.readableBytes());
		boolean escapePrefix = false; // 用0x5E，0x7D来代替0x7E; 用0x5E，0x5D来代替0x5E

		for (int index = byteBuf.readerIndex(); index < byteBuf.readerIndex() + byteBuf.readableBytes() - 1; index++) {
			byte peek = byteBuf.getByte(index);

			if (escapePrefix) { // 上一个字符为前缀字符
				escapePrefix = false; // 读取下个字符，前缀无效
				if (peek == ESCAPE_0x5E) {
					result.writeByte(0x5e);
					continue;
				} else if (peek == ESCAPE_0x7E) {
					result.writeByte(0x7e);
					continue;
				}
				result.writeByte(ESCAPE_PRE);
			}
			if (peek == ESCAPE_PRE) { // 当前字符为前缀字符
				escapePrefix = true;
			} else {
				result.writeByte(peek);
			}
		}
		return result;
	}

	/** CRC校验. */
	private static final boolean CRC(ByteBuf ptr) {
		int crc;
		short da;
		int[] crc_ta = { 0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50a5, 0x60c6, 0x70e7, 0x8108, 0x9129, 0xa14a, 0xb16b,
				0xc18c, 0xd1ad, 0xe1ce, 0xf1ef, };
		crc = 0;
		int index = 0;
		for (; index < ptr.readableBytes() - 2; index++) {
			short tmp = ptr.getUnsignedByte(index);

			da = (short) (((crc >> 8) & 0xFF) >> 4);
			crc <<= 4;
			crc ^= crc_ta[(int) ((da ^ (tmp >> 4)) & 0xFF)];

			da = (short) (((crc >> 8) & 0xFF) >> 4);
			crc <<= 4;
			crc ^= crc_ta[(int) (da ^ (tmp & 0x0f) & 0xFF)];
		}
		short ss = ptr.getShortLE(index);
		if ((short) (crc & 0xFFFF) == ss) {
			ptr.writerIndex(ptr.writerIndex() - 2); // 去掉crc数据，后续处理不需要
			return true;
		} else {
			return false;
		}
	}
}
