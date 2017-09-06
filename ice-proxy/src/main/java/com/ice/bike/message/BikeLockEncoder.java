package com.ice.bike.message;

import org.apache.commons.lang.StringUtils;

import com.ice.bike.contants.BCDConverter;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/***
 * 
 * 创建时间： 2017年8月26日 下午1:11:26
 * 
 * @author ice
 *
 */
public class BikeLockEncoder extends MessageToByteEncoder<BikeLockMessage> {

	private static final byte START_END = 0x7E;// 起始，结束

	@Override
	protected void encode(ChannelHandlerContext ctx, BikeLockMessage msg, ByteBuf out) throws Exception {
		serialize(msg, out);
	}

	private ByteBuf serialize(BikeLockMessage message, ByteBuf out) throws Exception {

		ByteBuf byteBuf = out.alloc().buffer();

		byteBuf.writeByte(message.getProtocolType());
		byteBuf.writeByte(message.getProtocolVersion());
		Long deviceId = Long.valueOf(message.getDeviceId());
		String devStr = String.format("%016d", deviceId);
		for (int i = 14; i >= 0; i -= 2) {
			byteBuf.writeByte((byte) Integer.parseInt(devStr.substring(i, i + 2)));
		}
		byteBuf.writeByte(message.getDeviceType());
		byteBuf.writeByte(message.getCommand());
		byteBuf.writeByte(message.getReply());

		BikeStatusReport report = message.getReport();
		/* 数据单元 */
		if (message.getCommand() == 0xA0) {
			if (report.getLock() != null) { /* 锁的状态(uint1型，1：关闭 ，2：打开 ， 0：不使用). */
				byteBuf.writeByte(0x04);
				byteBuf.writeShortLE(0x0010);
				byteBuf.writeByte(report.getLock());
			}
			// 0x04（数据长度）+0x0053(GPS标识) +(0x00/0x01)(基站或者GPS有效)
			if (report.getGprsItme() != null) {
				byteBuf.writeByte(0x40);
				byteBuf.writeShortLE(0x0053);
				byteBuf.writeByte(report.getGprsItme());
			}
			// 锁的电量, 0x04（数据长度）+0x0051(锁的电量) +(0x00- 0x64)(锁的电量0%-100%)
			if (report.getElectricity() != null) {
				byteBuf.writeByte(0x40);
				byteBuf.writeShortLE(0x0051);
				byteBuf.writeByte(report.getElectricity());
			}
			if (report.getGpsNumber() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x010A);
				byteBuf.writeByte(report.getGpsNumber());
			}
			// +0x0A (数据长度) +0x0150(时间)+7Byte(用BCD码标识)
			if (report.getUpTime() != null) {
				byteBuf.writeByte(10);
				byteBuf.writeShortLE(0x0150);
				byte[] bcdTime = BCDConverter.str2Bcd(report.getUpTime());
				byteBuf.writeBytes(bcdTime);
			}
			// +0x04 (数据长度) +0x010B（GPRS连接状态）+0x00(可断开)
			if (report.getGprsContextByte() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x010B);
				byteBuf.writeByte(report.getGprsContextByte());
			}
			// +0x07（数据长度）+0x0052(用户ID ) +4Byte（用户ID信息）
			if (report.getUserId() != null) {
				byteBuf.writeByte(7);
				byteBuf.writeShortLE(0x0052);
				byteBuf.writeIntLE(report.getUserId().intValue());
			}
			// +0x07（数据长度）+0x0108( 开锁时长)+4Byte(单位：秒)
			if (report.getOpenDeviceTimeLong() != null) {
				byteBuf.writeByte(7);
				byteBuf.writeShortLE(0x0108);
				byteBuf.writeIntLE(report.getOpenDeviceTimeLong().intValue());
			}
		} else if (message.getCommand() == 2) {
			// 查询状态
			// 0x04（数据长度）+0x0003(设备类别)+1byte(设备类别信息)
			byteBuf.writeByte(4); // 长度
			byteBuf.writeShortLE(0x0003);
			byteBuf.writeByte(Byte.valueOf("1"));

		} else {
			// 锁的状态 1：关闭 ，2：打开 ， 0：不使用 0x0010
			if (report.getLock() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0010);
				byteBuf.writeByte(report.getLock());
			}
			// 异常信息 1：关闭异常，2：打开异常，0：正常 0x0050
			if (report.getException() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0050);
				byteBuf.writeByte(report.getException());
			}
			// 锁的电量, 0%-100% 0x0051
			if (report.getElectricity() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0051);
				byteBuf.writeByte(report.getElectricity());
			}
			// 经度 0x0006
			if (report.getLongitude() != null) {
				byteBuf.writeByte(23); // 长度
				byteBuf.writeShortLE(0x0006);
				byteBuf.writeBytes(byteString(report.getLongitude()));
			}
			// 纬度 0x0007
			if (report.getLatitude() != null) {
				byteBuf.writeByte(23); // 长度
				byteBuf.writeShortLE(0x0007);
				byteBuf.writeBytes(byteString(report.getLatitude()));
			}
			// 告警 0x0301
			if (report.getBatteryWarring() != null && report.getLocWarring() != null && report.getFaultWarring() != null
					&& report.getOtherWarring() != null) {
				byteBuf.writeByte(7);
				byteBuf.writeShortLE(0x0301);
				byteBuf.writeByte(report.getBatteryWarring());
				byteBuf.writeByte(report.getLocWarring());
				byteBuf.writeByte(report.getFaultWarring());
				byteBuf.writeByte(report.getOtherWarring());
			}
			// 0x0105 mac地址
			if (report.getMacLong() != null) {
				byteBuf.writeByte(9);
				byteBuf.writeShortLE(0x0105);
				byteBuf.writeBytes(strTem(report.getMacLong()));
			}
			// 上报时间0x0150
			if (report.getUpTime() != null) {
				byteBuf.writeByte(10);
				byteBuf.writeShortLE(0x0150);
				byte[] bcdTime = BCDConverter.str2Bcd(report.getUpTime());
				byteBuf.writeBytes(bcdTime);
			}
			// 设备厂商代码 0x0002
			if (report.getVendorCode() != null) {
				byteBuf.writeByte(4); // 长度
				byteBuf.writeShortLE(0x0002);
				byteBuf.writeByte(report.getVendorCode());
			}
			// 设备类别 0x0003
			if (report.getDeviceType() != null) {
				byteBuf.writeByte(4); // 长度
				byteBuf.writeShortLE(0x0003);
				byteBuf.writeByte(report.getDeviceType());
			}
			// 设备型号 0x0004
			if (report.getDeviceNumber() != null) {
				byteBuf.writeByte(23); // 长度
				byteBuf.writeShortLE(0x0004);
				byteBuf.writeBytes(byteString(report.getDeviceNumber()));
			}
			// 设备生产序列号 0x0005
			if (report.getSerialNum() != null) {
				byteBuf.writeByte(23); // 长度
				byteBuf.writeShortLE(0x0005);
				byteBuf.writeBytes(byteString(report.getSerialNum()));
			}
			// 监控版本信息 0x0008
			if (report.getMonitorVersion() != null) {
				byteBuf.writeByte(23); // 长度
				byteBuf.writeShortLE(0x0008);
				byteBuf.writeBytes(byteString(report.getMonitorVersion()));
			}
			// IMSI 0x0009
			if (report.getImsi() != null) {
				byteBuf.writeByte(23); // 长度
				byteBuf.writeShortLE(0x0009);
				byteBuf.writeBytes(byteString(report.getImsi()));
			}
			// 单车设备号 0x0101
			if (report.getDeviceCode() != null) {
				byteBuf.writeByte(11);
				byteBuf.writeShortLE(0x0101);
				byteBuf.writeLongLE(report.getDeviceCode());
			}
			// 设备类型 0x0102
			if (report.getDeviceCodeType() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0102);
				byteBuf.writeByte(report.getDeviceCodeType());
			}
			// 设备密码 0x0103
			if (report.getDevicePassword() != null) {
				byteBuf.writeByte(7);
				byteBuf.writeShortLE(0x0103);
				byteBuf.writeIntLE(report.getDevicePassword().intValue());
			}
			// 开锁开关 0x0104
			if (report.getOpenDevice() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0104);
				byteBuf.writeByte(report.getOpenDevice());
			}
			// gprs工作模式 0x0106
			if (report.getGprsWord() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0106);
				byteBuf.writeByte(report.getGprsWord());
			}
			// 监控中心域名0x0110
			if (report.getListenAre() != null) {
				byteBuf.writeByte(23); // 长度
				byteBuf.writeShortLE(0x0110);
				byteBuf.writeBytes(byteString(report.getListenAre()));
			}
			// 监控中心ip 0x0111
			if (report.getListenIp() != null) {
				byteBuf.writeByte(7);
				byteBuf.writeShortLE(0x0111);
				byteBuf.writeIntLE(report.getListenIp().intValue());
			}
			// 监控中心端口 0x0112
			if (report.getListenPort() != null) {
				byteBuf.writeByte(5);
				byteBuf.writeShortLE(0x0112);
				// byteBuf.writeShortLE(report.getListenPort().intValue());
				byteBuf.writeByte((byte) ((report.getListenPort() >> 0) & 0xFF));
				byteBuf.writeByte((byte) ((report.getListenPort() >> 8) & 0xFF));

				// ByteBuffer buffer = ByteBuffer.allocate(1024);
				// buffer.putInt(report.getPort());
				// buffer.flip();
				// for (int i = buffer.remaining() - 1; i > 1; i--) {
				// System.out.print(String.format("%02x ", buffer.get(i)));
				// byteBuf.writeByte(buffer.get(i));
				// }

			}
			// 上报通信方式 0x0140
			if (report.getUpSocket() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0140);
				byteBuf.writeByte(report.getUpSocket());
			}
			// 上报类型 0x0141
			if (report.getRepType() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0141);
				byteBuf.writeByte(report.getRepType());
			}
			// 用户id 0x0052
			if (report.getUserId() != null) {
				byteBuf.writeByte(7);
				byteBuf.writeShortLE(0x0052);
				byteBuf.writeIntLE(report.getUserId().intValue());
			}
			// 0x0108 开锁时长
			if (report.getOpenDeviceTimeLong() != null) {
				byteBuf.writeByte(7);
				byteBuf.writeShortLE(0x0108);
				byteBuf.writeIntLE(report.getOpenDeviceTimeLong().intValue());
			}
			// GPS信息标识 0x0053
			if (report.getGprsItme() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0053);
				byteBuf.writeByte(report.getGprsItme());
			}
			// 0x0107 GPRS定时上报间隔时长
			if (report.getGprsTimeLong() != null) {
				byteBuf.writeByte(7);
				byteBuf.writeShortLE(0x0107);
				byteBuf.writeIntLE(report.getGprsTimeLong().intValue());
			}
			// 0x010A GPS卫星个数
			if (report.getGpsNumber() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x010A);
				byteBuf.writeByte(report.getGpsNumber());
			}
			// 0x0901 升包类型
			if (report.getPackageType() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0901);
				byteBuf.writeByte(report.getPackageType());
			}
			// 0x0902 包序号
			if (report.getPackageItem() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0902);
				byteBuf.writeByte(report.getPackageItem());
			}
			// 0x0903 升级包长度
			if (report.getPackageLong() != null) {
				byteBuf.writeByte(5);
				byteBuf.writeShortLE(0x0903);
				byteBuf.writeShortLE(report.getPackageLong().intValue());
			}
			// 0x010B GPRS连接状态设置 Uint1型 0：可以断开 1：维持连接（服务器有指令下发，GPRS需要维持连接
			if (report.getGprsContextByte() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x010B);
				byteBuf.writeByte(report.getGprsContextByte());
			}
			// 软件升级类型 0x010C
			if (report.getUpByte() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x010C);
				byteBuf.writeByte(report.getUpByte());
			}
			// 升级包地址 Str型，长度60个字节 0x010D
			if (report.getUpPackagePath() != null) {
				byteBuf.writeByte(63); // 长度
				byteBuf.writeShortLE(0x010D);
				byteBuf.writeBytes(byteStringBig(report.getUpPackagePath()));
			}
			// 更改模式
			if (report.getProductMode() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0151);
				byteBuf.writeByte(report.getProductMode());
			}
			// 参数设置：0x07（数据长度）+0x0152(心跳)+心跳值(4byte) +
			if (report.getPulse() != null) {
				byteBuf.writeByte(7); // 长度
				byteBuf.writeShortLE(0x0152);
				byteBuf.writeIntLE(report.getPulse().intValue());
			}
			// 参数设置：0x04（数据长度）+0x0154(进入蓝牙模式电量)+电量值(1byte)
			if (report.getElecValue() != null) {
				byteBuf.writeByte(4); // 长度
				byteBuf.writeShortLE(0x0154);
				byteBuf.writeByte(report.getElecValue());
			}
			// 服务器同步时间 0x0150
			if (report.getServerTime() != null) {
				byteBuf.writeByte(10);
				byteBuf.writeShortLE(0x0150);
				byte[] bcdTime = BCDConverter.str2Bcd(report.getServerTime());
				byteBuf.writeBytes(bcdTime);
			}
			// 长连接时间区间 0x1401
			if (!StringUtils.isEmpty(report.getTimeInterval())) {
				byte[] domainByte = byteChgString(report.getTimeInterval());
				byteBuf.writeByte(3 + domainByte.length); // 长度
				byteBuf.writeShortLE(0x1401);
				byteBuf.writeBytes(domainByte);
			}

			// 0x04（数据长度）+0x0113(gps上报类型)+1byte(gps上报类型)
			if (report.getGspType() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0113);
				byteBuf.writeByte(report.getGspType());
			}

			// 0x04（数据长度）+0x0114(csq信号)+ 1byte(csq信号)
			if (report.getCspIdx() != null) {
				byteBuf.writeByte(4);
				byteBuf.writeShortLE(0x0114);
				byteBuf.writeByte(report.getCspIdx());
			}
		}
		
		// CRC 检验单元
		byteBuf.writeShortLE(CRC(byteBuf));
		ByteBuf byteBuf1 = out.alloc().buffer();
		// 转义处理
		/**
		 * 由于使用16进制方式传输数据，为防止数据中出现与通信包起始标志、结束标志相同的数据而影响这两个标志的判断。在发送和接收时必须进行数据的转义
		 * ，使用的转义字符是ASCII字符‘^’（0x5E）。范围：在本层协议中，
		 * 对除起始标志和结束标志外的所有数据。转义规则：用0x5E，0x5D来代替0x5E；用0x5E，0x7D来代替0x7E。
		 * 对于需要进行转义后发送的情况，要按照如下的顺序进行操作：发送数据包之前，先生成CRC校验值，再进行转义处理；接收到数据包后，先进行转义处理
		 * ，再进行CRC校验。
		 */
		while (byteBuf.readableBytes() > 0) {
			byte org = byteBuf.readByte();
			if (org == 0x5E) {
				byteBuf1.writeByte(0x5E);
				byteBuf1.writeByte(0x5D);
			} else if (org == 0x7E) {
				byteBuf1.writeByte(0x5E);
				byteBuf1.writeByte(0x7D);
			} else {
				byteBuf1.writeByte(org);
			}
		}

		out.writeByte(BikeLockEncoder.START_END);
		out.writeBytes(byteBuf1);
		out.writeByte(BikeLockEncoder.START_END);
		return out;
	}

	private final int CRC(ByteBuf ptr) {
		int crc;
		short da;

		int[] crc_ta = { 0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50a5, 0x60c6, 0x70e7, 0x8108, 0x9129, 0xa14a, 0xb16b,
				0xc18c, 0xd1ad, 0xe1ce, 0xf1ef, };
		crc = 0;
		int index = 0;
		// while(ptr.readableBytes() - index >= 2) {

		for (; index < ptr.readableBytes(); index++) {
			short tmp = ptr.getUnsignedByte(index);

			da = (short) (((crc >> 8) & 0xFF) >> 4);
			crc <<= 4;
			crc ^= crc_ta[(int) ((da ^ (tmp >> 4)) & 0xFF)];

			da = (short) (((crc >> 8) & 0xFF) >> 4);
			crc <<= 4;
			crc ^= crc_ta[(int) (da ^ (tmp & 0x0f) & 0xFF)];
		}

		return (crc);

	}

	public final byte[] byteString(String str) {
		byte[] tmp = new byte[20];
		byte[] org = str.getBytes();
		for (int i = 0; i < tmp.length && i < org.length; i++) {
			tmp[i] = org[i];
		}
		return tmp;
	}

	public final byte[] byteChgString(String chgStr) {
		byte[] org = chgStr.getBytes();
		byte[] tmp = new byte[org.length];
		for (int i = 0; i < tmp.length && i < org.length; i++) {
			tmp[i] = org[i];
		}
		return tmp;
	}

	public final byte[] byteStringBig(String str) {
		byte[] tmp = new byte[60];
		byte[] org = str.getBytes();
		for (int i = 0; i < tmp.length && i < org.length; i++) {
			tmp[i] = org[i];
		}
		return tmp;
	}

	public final byte[] strTem(String mac) {

		byte[] data = mac.getBytes();
		byte[] macData = new byte[6];
		for (int i = 0; i < data.length && i < macData.length; i++) {
			macData[macData.length - 1 - i] = data[i];
		}
		return macData;
	}
}
