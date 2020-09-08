/*
 * Author Christian Gausepohl
 * License: CC0 (no copyright if possible, otherwise fallback to public domain)
 * https://github.com/cgausepohl/gcli
 */

package com.cg.cli;

@SuppressWarnings("serial")
public class CLIRuleException extends Exception {

	public CLIRuleException() {
	}

	public CLIRuleException(String message) {
		super(message);
	}

	public CLIRuleException(Throwable cause) {
		super(cause);
	}

	public CLIRuleException(String message, Throwable cause) {
		super(message, cause);
	}

	public CLIRuleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
