package com.canyue.mqtt.core.packet;

/**
 * @author canyue
 */

public enum PacketType {
	//mqtt 14种报文类型
	CONNECT_TYPE(1,"connect"),
	CONNACK_TYPE(2,"connect ack"),
	PUBLISH_TYPE(3,"publish"),
	PUBACK_TYPE(4,"publish ack"),
	PUBREC_TYPE(5,"publish rec"),
	PUBREL_TYPE(6,"publish rel"),
	PUBCOMP_TYPE(7,"publish comp"),
	SUBSCRIBE_TYPE(8,"subscribe"),
	SUBACK_TYPE(9,"subscribe ack"),
	UNSUBSCRIBE_TYPE(10,"unsubscribe"),
	UNSUBACK_TYPE(11,"unsubscribe ack"),
	PINGREQ_TYPE(12,"ping req"),
	PINGRESP_TYPE(13,"ping resp"),
	DISCONNECT_TYPE(14,"disconnect");
	private String desc;
	private int value;

	private PacketType(int value,String desc){
		this.value =value;
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return desc;
	}
}
