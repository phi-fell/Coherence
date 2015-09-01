package io.proffitt.coherence.error;

public class ErrorHandler {
	static ErrorHandler	handler	= null;
	public static ErrorHandler get() {
		if (handler == null) {
			handler = new ErrorHandler();
		}
		return handler;
	}
	private ErrorHandler() {
	}
	public void handle(boolean b) {
		handle(b, ErrorCode.UNSPECIFIED);
	}
	public void handle(boolean b, int code) {
		if (b) {
			return;
		} else {
			throw new RuntimeException("Error Code: " + code);
		}
	}
}
