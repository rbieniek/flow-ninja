/**
 * 
 */
package org.flowninja.persistence.rspl.redis.components;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.flowninja.types.net.CIDR4Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author rainer
 *
 */
public class CIDR4AddressRedisSerializer implements RedisSerializer<CIDR4Address> {
	private static final Logger logger = LoggerFactory.getLogger(CIDR4AddressRedisSerializer.class);

	private Charset charset = Charset.forName("ASCII");
	
	/* (non-Javadoc)
	 * @see org.springframework.data.redis.serializer.RedisSerializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(CIDR4Address address) throws SerializationException {
		try {
			StringBuilder builder = new StringBuilder("cidr:");
	
			logger.info("encoding CIDR address {}", address);
			
			builder.append(Hex.encodeHex(address.getAddress(), true));
			builder.append("/");
			builder.append(Integer.toString(address.getNetmask()));
			
			String key = builder.toString();
			
			logger.info("encoded address {} to key {}", address, key);
			
			return key.getBytes(charset);
		} catch(Exception e) {
			throw new SerializationException("failed to serialize key: " + address, e);
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.redis.serializer.RedisSerializer#deserialize(byte[])
	 */
	@Override
	public CIDR4Address deserialize(byte[] bytes) throws SerializationException {
		try {
			CIDR4Address address = null;
			String key = new String(bytes, charset);

			logger.info("deserializing key {}", key);
			
			if(StringUtils.startsWith(key, "cidr:")) {
				String[] parts = StringUtils.split(StringUtils.substring(key, 5), "/");
				
				if(parts != null && parts.length == 2)
					address = new CIDR4Address(Hex.decodeHex(parts[0].toCharArray()), Integer.parseInt(parts[1]));
			}

			logger.info("deserialized key {} to address {}", key, address);
			
			return address;
		} catch(Exception e) {
			logger.error("cannot deserialize key {}", bytes, e);
			
			throw new SerializationException("cannot deserialize passed key", e);
		}
	}

}
