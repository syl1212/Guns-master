package com.thirdparty.alioss.enu;

/**
 * @Author syl
 */
public enum UploadFileResultEnum {
	/**
	 * 上传类型异常
	 */
	ILLEGAL_FILE_TYPE(1,"文件上传失败(文件类型不正确)！"),
	/**
	 * 压缩异常
	 */
	COMPRESS_ERROR(2,"文件压缩异常"),
	/**
	 * 写入异常
	 */
	WRITE_ERROR(3,"文件写入异常"),
	/**
	 * 上传异常
	 */
	UPLOAD_ERROR(2,"文件上传异常");
	
	private int code;
	private String message;
	
	private UploadFileResultEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
	
}
