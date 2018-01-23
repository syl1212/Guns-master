/**
 * 
 */
package com.thirdparty.alioss.exception;


/**
 * @author yuanli.sun
 *
 */
public class UploadFileException extends BasicException {
	private static final long serialVersionUID = 4094257856069829461L;

	public UploadFileException() {
		super();
	}
	
	public UploadFileException(String message) {
		super(message);
	}
	
	public UploadFileException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
