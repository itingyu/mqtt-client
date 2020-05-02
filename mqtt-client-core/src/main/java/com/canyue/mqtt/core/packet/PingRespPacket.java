package com.canyue.mqtt.core.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author canyue
 */
public class PingRespPacket extends BasePacket {
	
	private final  PacketType type = PacketType.PINGRESP_TYPE;
	private static Logger logger = LoggerFactory.getLogger(PingRespPacket.class);
	
	public PingRespPacket(byte[] data) {
		logger.debug("ping resp 报文解析完毕！");
	}
	
	@Override
    public byte[] getVariableHeader() throws IOException {
        return new byte[0];
    }

    @Override
    public byte[] getPayload() throws IOException {
        return new byte[0];
    }

    @Override
    public byte getFixHeaderFlag() {
        return 0;
    }
	@Override
    public PacketType getType() {
		return type;
	}
}
