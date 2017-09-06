package com.ice.bike.codec;

import io.netty.handler.codec.DecoderException;

/**
 * 
 * 创建时间： 2017年9月3日 上午10:37:09
 * 
 * @author ice
 *
 */
public class BikeLockMessageInvalidException extends DecoderException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance.
	 */
	public BikeLockMessageInvalidException() {
	}

	/**
	 * Creates a new instance.
	 */
	public BikeLockMessageInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new instance.
	 */
	public BikeLockMessageInvalidException(String message) {
		super(message);
	}

	/**
	 * Creates a new instance.
	 */
	public BikeLockMessageInvalidException(Throwable cause) {
		super(cause);
	}
}
