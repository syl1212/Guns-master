package com.thirdparty.alioss.vo;

import java.io.Serializable;
import java.util.Arrays;

public class UploadFileDTO implements Serializable {
	private static final long serialVersionUID = 3980076904499581368L;

	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * 文件存储完整路径名
	 */
	private String fileStorePathName;
	
	
	/**
	 * 文件后缀
	 */
	private String fileSuffix;

	/**
	 * 文件描述
	 */
	private String fileDesc;

	/**
	 * 文件流
	 */
	private byte[] input;
	
	/**
	 * 文件大小
	 */
	private Long fileLength;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileStorePathName() {
		return fileStorePathName;
	}

	public void setFileStorePathName(String fileStorePathName) {
		this.fileStorePathName = fileStorePathName;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getFileDesc() {
		return fileDesc;
	}

	public void setFileDesc(String fileDesc) {
		this.fileDesc = fileDesc;
	}

	public byte[] getInput() {
		return input;
	}

	public void setInput(byte[] input) {
		this.input = input;
	}

	public Long getFileLength() {
		return fileLength;
	}

	public void setFileLength(Long fileLength) {
		this.fileLength = fileLength;
	}

	@Override
	public String toString() {
		return "UploadFileDTO{" +
				"fileName='" + fileName + '\'' +
				", fileStorePathName='" + fileStorePathName + '\'' +
				", fileSuffix='" + fileSuffix + '\'' +
				", fileDesc='" + fileDesc + '\'' +
				", input=" + Arrays.toString(input) +
				", fileLength=" + fileLength +
				'}';
	}
}
