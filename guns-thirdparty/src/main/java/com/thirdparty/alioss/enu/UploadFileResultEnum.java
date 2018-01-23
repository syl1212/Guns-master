package com.thirdparty.alioss.enu;

public enum UploadFileResultEnum {
	
	illegalFileType(1,"文件上传失败(文件类型不正确)！"),
	compressError(2,"文件压缩异常"),
	writeError(3,"文件写入异常"),
	uploadError(2,"文件上传异常");
	
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
