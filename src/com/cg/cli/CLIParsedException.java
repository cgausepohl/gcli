/*
 * Author Christian Gausepohl
 * License: CC0 (no copyright if possible, otherwise fallback to public domain)
 * https://github.com/cgausepohl/gcli
 */

package com.cg.cli;

@SuppressWarnings("serial")
public class CLIParsedException extends Exception {

	public CLIParsedException() {
	}

	public CLIParsedException(String message) {
		super(message);
	}

	public CLIParsedException(Throwable cause) {
		super(cause);
	}

	public CLIParsedException(String message, Throwable cause) {
		super(message, cause);
	}

	public CLIParsedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
