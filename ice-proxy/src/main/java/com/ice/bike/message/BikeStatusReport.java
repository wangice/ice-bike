package com.ice.bike.message;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.ice.bike.codec.BikeLockMessageInvalidException;
import com.ice.bike.contants.BCDConverter;

import io.netty.buffer.ByteBuf;

/**
 * 
 * 创建时间： 2017年9月2日 上午1:07:47
 * 
 * @author ice
 *
 */
public class BikeStatusReport implements Serializable {

	private static final long serialVersionUID = 1L;

	private Byte vendorCode; // 设备厂商代码 0x0002
	private Byte deviceType; // 设备类别 0x0003
	private String deviceNumber; // 设备型号 0x0004
	private String serialNum; // 设备生产序列号 0x0005
	private String longitude; // 经度 0x0006
	private String latitude; // 纬度 0x0007
	private String monitorVersion; // 监控版本信息 0x0008
	private String imsi; // IMSI 0x0009
	private Byte lock; // 锁的状态 1：关闭 ，2：打开 ， 0：不使用 0x0010
	private Byte exception; // 异常信息 uint1型 ，1：关闭异常，2：打开异常，0：正常3:不在范围内，4.打开超时
							// 0x0050
	private Byte electricity; // 锁的电量, 0%-100% 0x0051

	private Long userId; // 用户id 0x0052
	private Byte gprsItme; // GPS信息标识 0x0053

	private Long deviceCode; // 单车设备号 0x0101
	private Byte deviceCodeType; // 设备类型 0x0102
	private Long devicePassword; // 设备密码 0x0103
	private Byte openDevice; // 开锁开关 0x0104

	private String macLong;// 0x0105 mac地址
	private Byte gprsWord; // gprs工作模式 0x0106
	private Long gprsTimeLong; // 0x0107 GPRS定时上报间隔时长
	private Long openDeviceTimeLong; // 0x0108 开锁时长
	private Byte gpsNumber; // 0x010A GPS卫星个数

	private String listenAre; // 监控中心域名0x0110//
								// 0x17（数据长度）+0x0110(域名)+域名字符串(20byte)+
	private Long listenIp; // 监控中心ip 0x0111
	private Long listenPort; // 监控中心端口 0x0112 //
								// 0x07（数据长度）+0x0112(端口)+端口号(4byte)+
	private Byte upSocket; // 上报通信方式 0x0140
	private Byte repType; // 上报类型 0x0141
	private String upTime; // 上报时间0x0150
	private Byte batteryWarring; // 告警 0x0301
	private Byte locWarring; // 位置告警
	private Byte faultWarring; // 故障告警
	private Byte otherWarring; // 其他告警

	private Byte packageType; // 0x0901 升包类型
	private Byte packageItem; // 0x0902 包序号
	private Long packageLong; // 0x0903 升级包长度

	private Byte gprsContextByte; // 0x010B GPRS连接状态设置 Uint1型 0：可以断开
									// 1：维持连接（服务器有指令下发，GPRS需要维持连接
	private Byte upByte;// 软件升级类型 0x010C
	private String upPackagePath; // 升级包地址 Str型，长度60个字节 0x010D

	// 0x04（数据长度）+0x0151(更改模式)+0x01(生产模式)
	private Byte productMode; // 0x01(生产模式)
	// 设置工作参数配置
	// 0x07（数据长度）+0x0152(心跳)+心跳值(4byte) +
	private Long pulse;
	// 0x04（数据长度）+0x0154(进入蓝牙模式电量)+电量值(1byte)+
	private Byte elecValue;

	// 0xxx（数据长度）+0x1401(时间区间)+时间区间字符串(xxbyte)+
	private String timeInterval;

	// 0x0A(数据长度)+0x0150(时间)+7Byte(用BCD码标识)
	private String serverTime; // 服务时间0x0150
	// 0x04（数据长度）+0x0114(csq信号)+ 1byte(csq信号)
	private Byte cspIdx;
	// 0x04（数据长度）+0x0113(gps上报类型)+1byte(gps上报类型)
	private Byte gspType;
	public static final int VENDOR_CODE = 0x0002;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(64);
		builder.append("BikeLockStatus : ");
		try {
			Map<String, String> describe = BeanUtils.describe(this);
			Iterator<Map.Entry<String, String>> iterator = describe.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				if (entry.getKey() == null || entry.getKey().equals("class")) {
					continue;
				}
				if (entry.getValue() == null) {
					continue;
				}
				builder.append(entry.getKey());
				builder.append('=');
				builder.append(entry.getValue());
				builder.append(", ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			builder.append(" parse with exception ");
			builder.append(e.getMessage());
		}
		return builder.toString();
	}

	// todo ： 有些响应内容未添加参照文档自行添加，并修正构造函数
	/**
	 *
	 * @param buf
	 * @return
	 */
	private String readString(ByteBuf buf, int length) {
		byte[] tmpBuf = new byte[length - 3];
		buf.readBytes(tmpBuf);
		return new String(tmpBuf);
	}

	private Byte readByte(ByteBuf buf) {
		return new Byte(buf.readByte());
	}

	private String readTime(ByteBuf buf, int length) {
		byte[] tmp = new byte[length - 3];
		buf.readBytes(tmp);
		return BCDConverter.bcd2Str(tmp);
	}

	private Long readLong(ByteBuf buf) {
		return new Long(buf.readLongLE());
	}

	private Long readInt(ByteBuf buf) {
		return new Long(buf.readIntLE());
	}

	private String readLongs(ByteBuf buf, int length) {
		byte[] tmpBuf = new byte[length - 3];
		buf.readBytes(tmpBuf);
		String value = "";
		for (int i = tmpBuf.length - 1; i >= 0; i--) {
			String sTemp = Integer.toHexString(tmpBuf[i] & 0xFF);
			if (sTemp.length() == 1) {
				sTemp = '0' + sTemp;
			}
			value = value + sTemp;
		}
		value = value.toUpperCase();
		return value;
	}

	private void skipUnknwonType(ByteBuf buf, int length) {
		buf.skipBytes(length - 3); // 已经读出长度和类型三个字节
	}

	public BikeStatusReport() {

	}

	public BikeStatusReport(ByteBuf buf) {
		while (buf.readableBytes() > 0) {
			int len = buf.readByte();
			if (buf.readableBytes() < 0) {
				throw new BikeLockMessageInvalidException("Invalid status item");
			} else if (buf.readableBytes() == 0) {
				break;
			}
			int type = buf.readUnsignedShortLE();

			switch (type) {
			case VENDOR_CODE:
				vendorCode = readByte(buf);
				break;
			case 0x0003:
				deviceType = readByte(buf);
				break;
			case 0x0004:
				deviceNumber = readString(buf, len);
				break;
			case 0x0005:
				serialNum = readString(buf, len);
				break;
			case 0x0006:
				longitude = readString(buf, len);
				break;
			case 0x0007:
				latitude = readString(buf, len);
				break;
			case 0x0008:
				monitorVersion = readString(buf, len);
				break;
			case 0x0009:
				imsi = readString(buf, len);
				break;
			case 0x0010:
				lock = readByte(buf);
				break;
			case 0x0050:
				exception = readByte(buf);
				break;
			case 0x0051:
				electricity = readByte(buf);
				break;
			case 0x0141:
				repType = readByte(buf);
				break;
			case 0x0101:
				deviceCode = readLong(buf);
				break;
			case 0x0102:
				deviceCodeType = readByte(buf);
				break;
			case 0x0103:
				devicePassword = readInt(buf);
				break;
			case 0x0104:
				openDevice = readByte(buf);
				break;
			case 0x0106:
				gprsWord = readByte(buf);
				break;
			case 0x0110:
				monitorVersion = readString(buf, len);
				break;
			case 0x0111:
				listenIp = readInt(buf);
				break;
			case 0x0112:
				listenPort = (long) buf.readShortLE();
				break;
			case 0x0140:
				upSocket = readByte(buf);
				break;
			case 0x0150:
				upTime = readTime(buf, len);
				break;
			case 0x0301:
				batteryWarring = readByte(buf);
				locWarring = readByte(buf);
				faultWarring = readByte(buf);
				otherWarring = readByte(buf);
				break;
			case 0x0052:
				userId = readInt(buf);
				break;
			case 0x0053:
				gprsItme = readByte(buf);
				break;
			case 0x0105:
				macLong = readLongs(buf, len);
				break;
			case 0x0107:
				gprsTimeLong = readInt(buf);
				break;
			case 0x0108:
				openDeviceTimeLong = readInt(buf);
				break;
			case 0x010A:
				gpsNumber = readByte(buf);
				break;
			case 0x0901:
				packageType = readByte(buf);
				break;
			case 0x0902:
				packageItem = readByte(buf);
				break;
			case 0x0903:
				packageLong = (long) buf.readShortLE();
				break;
			case 0x010B:
				gprsContextByte = readByte(buf);
				break;
			case 0x010C:
				upByte = readByte(buf);
				break;
			case 0x010D:
				upPackagePath = readString(buf, len);
				break;
			case 0x0151:
				productMode = readByte(buf);
				break;
			case 0x0152:
				pulse = readInt(buf);
				break;
			case 0x0154:
				elecValue = readByte(buf);
				break;
			case 0x0113:
				gspType = readByte(buf);
				break;
			case 0x0114:
				cspIdx = readByte(buf);
				break;
			case 0x1401:
				timeInterval = readString(buf, len);
				break;
			default:
				skipUnknwonType(buf, len);
				break;
			}
		}
	}

	public Byte getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(Byte vendorCode) {
		this.vendorCode = vendorCode;
	}

	public Byte getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Byte deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getSerialNum() {
		return StringUtils.isNotEmpty(serialNum) ? serialNum.trim() : serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getLongitude() {
		return StringUtils.isNotEmpty(longitude) ? longitude.replace("W", "-").replace("E", "").trim() : longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return StringUtils.isNotEmpty(latitude) ? latitude.replace("S", "-").replace("N", "").trim() : latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getMonitorVersion() {
		return StringUtils.isNotEmpty(monitorVersion) ? monitorVersion.trim() : monitorVersion;
	}

	public void setMonitorVersion(String monitorVersion) {
		this.monitorVersion = monitorVersion;
	}

	public String getImsi() {
		return StringUtils.isNotEmpty(imsi) ? imsi.trim() : imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public Byte getLock() {
		return lock;
	}

	public void setLock(Byte lock) {
		this.lock = lock;
	}

	public Byte getException() {
		return exception;
	}

	public void setException(Byte exception) {
		this.exception = exception;
	}

	public Byte getElectricity() {
		return electricity;
	}

	public void setElectricity(Byte electricity) {
		this.electricity = electricity;
	}

	public Long getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(Long deviceCode) {
		this.deviceCode = deviceCode;
	}

	public Byte getDeviceCodeType() {
		return deviceCodeType;
	}

	public void setDeviceCodeType(Byte deviceCodeType) {
		this.deviceCodeType = deviceCodeType;
	}

	public Long getDevicePassword() {
		return devicePassword;
	}

	public void setDevicePassword(Long devicePassword) {
		this.devicePassword = devicePassword;
	}

	public Byte getOpenDevice() {
		return openDevice;
	}

	public void setOpenDevice(Byte openDevice) {
		this.openDevice = openDevice;
	}

	public Byte getGprsWord() {
		return gprsWord;
	}

	public void setGprsWord(Byte gprsWord) {
		this.gprsWord = gprsWord;
	}

	public String getListenAre() {
		return listenAre;
	}

	public void setListenAre(String listenAre) {
		this.listenAre = listenAre;
	}

	public Long getListenIp() {
		return listenIp;
	}

	public void setListenIp(Long listenIp) {
		this.listenIp = listenIp;
	}

	public Long getListenPort() {
		return listenPort;
	}

	public void setListenPort(Long listenPort) {
		this.listenPort = listenPort;
	}

	public Byte getUpSocket() {
		return upSocket;
	}

	public void setUpSocket(Byte upSocket) {
		this.upSocket = upSocket;
	}

	public Byte getRepType() {
		return repType;
	}

	public void setRepType(Byte repType) {
		this.repType = repType;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	public Byte getBatteryWarring() {
		return batteryWarring;
	}

	public void setBatteryWarring(Byte batteryWarring) {
		this.batteryWarring = batteryWarring;
	}

	public Byte getLocWarring() {
		return locWarring;
	}

	public void setLocWarring(Byte locWarring) {
		this.locWarring = locWarring;
	}

	public Byte getFaultWarring() {
		return faultWarring;
	}

	public void setFaultWarring(Byte faultWarring) {
		this.faultWarring = faultWarring;
	}

	public Byte getOtherWarring() {
		return otherWarring;
	}

	public void setOtherWarring(Byte otherWarring) {
		this.otherWarring = otherWarring;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Byte getGprsItme() {
		return gprsItme;
	}

	public void setGprsItme(Byte gprsItme) {
		this.gprsItme = gprsItme;
	}

	public Long getGprsTimeLong() {
		return gprsTimeLong;
	}

	public String getMacLong() {
		return StringUtils.isNotEmpty(macLong) ? macLong.trim() : macLong;
	}

	public void setMacLong(String macLong) {
		this.macLong = macLong;
	}

	public void setGprsTimeLong(Long gprsTimeLong) {
		this.gprsTimeLong = gprsTimeLong;
	}

	public Long getOpenDeviceTimeLong() {
		return openDeviceTimeLong;
	}

	public void setOpenDeviceTimeLong(Long openDeviceTimeLong) {
		this.openDeviceTimeLong = openDeviceTimeLong;
	}

	public Byte getGpsNumber() {
		return gpsNumber;
	}

	public void setGpsNumber(Byte gpsNumber) {
		this.gpsNumber = gpsNumber;
	}

	public Byte getPackageType() {
		return packageType;
	}

	public void setPackageType(Byte packageType) {
		this.packageType = packageType;
	}

	public Byte getPackageItem() {
		return packageItem;
	}

	public void setPackageItem(Byte packageItem) {
		this.packageItem = packageItem;
	}

	public Long getPackageLong() {
		return packageLong;
	}

	public void setPackageLong(Long packageLong) {
		this.packageLong = packageLong;
	}

	public Byte getGprsContextByte() {
		return gprsContextByte;
	}

	public void setGprsContextByte(Byte gprsContextByte) {
		this.gprsContextByte = gprsContextByte;
	}

	public Byte getUpByte() {
		return upByte;
	}

	public void setUpByte(Byte upByte) {
		this.upByte = upByte;
	}

	public String getUpPackagePath() {
		return upPackagePath;
	}

	public void setUpPackagePath(String upPackagePath) {
		this.upPackagePath = upPackagePath;
	}

	public Byte getProductMode() {
		return productMode;
	}

	public void setProductMode(Byte productMode) {
		this.productMode = productMode;
	}

	public Long getPulse() {
		return pulse;
	}

	public void setPulse(Long pulse) {
		this.pulse = pulse;
	}

	public Byte getElecValue() {
		return elecValue;
	}

	public void setElecValue(Byte elecValue) {
		this.elecValue = elecValue;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public String getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}

	public Byte getCspIdx() {
		return cspIdx;
	}

	public void setCspIdx(Byte cspIdx) {
		this.cspIdx = cspIdx;
	}

	public Byte getGspType() {
		return gspType;
	}

	public void setGspType(Byte gspType) {
		this.gspType = gspType;
	}

}
