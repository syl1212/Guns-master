package com.thirdparty.alioss.service;

import com.thirdparty.alioss.exception.GetFileInfoFailedException;
import com.thirdparty.alioss.exception.UploadFileException;
import com.thirdparty.alioss.vo.UploadFileDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class AbstractFileRemoteService implements FileRemoteService {

	/* (non-Javadoc)
	 * @see com.yjf.yrd.qsc.service.file.FileRemoteService#uploadFile(com.yjf.yrd.qsc.service.file.UploadFileDTO)
	 */
	@Override
    public String uploadFile(UploadFileDTO uploadDTO)
			throws UploadFileException {
		if(StringUtils.isBlank(uploadDTO.getFileName())){
			throw new UploadFileException("upload fileName is empty");
		}
		
		if(StringUtils.indexOf(uploadDTO.getFileName(), ".") == -1){
			throw new UploadFileException("upload fileName ["+uploadDTO.getFileName()+"] is invalid");
		}
		return null;
	}
	
	/**
	 * 检查文件后缀是否合法
	 * @param fileSuffix
	 * @throws UploadFileException
	 */
	protected void validateFileSuffix(String fileSuffix) throws UploadFileException {
		if(StringUtils.isBlank(fileSuffix)){
			throw new UploadFileException("upload fileSuffix is empty");
		}
	}
	
	protected String getContentType(String fileSuffix) {
		if(StringUtils.isBlank(fileSuffix)) {
			return null;
		}
		fileSuffix = fileSuffix.toLowerCase();
		if(fileSuffix.indexOf("jpg") > -1 || fileSuffix.indexOf("jpeg") > -1) {
			return "image/jpeg";
		}
		if(fileSuffix.indexOf("gif") > -1) {
			//return "image/gif";
			return "image/jpeg";
		}
		if(fileSuffix.indexOf("bmp") > -1) {
			//return "application/x-bmp";
			return "image/jpeg";
		}
		if(fileSuffix.indexOf("png") > -1) {
			//return "image/png";
			return "image/jpeg";
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yjf.yrd.qsc.service.file.FileRemoteService#downloadFile(java.lang.String)
	 */
	@Override
    public byte[] downloadFile(String fileName) throws GetFileInfoFailedException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.yjf.yrd.qsc.service.file.FileRemoteService#exists(java.lang.String)
	 */
	@Override
    public boolean exists(String fileName) throws GetFileInfoFailedException {
		return false;
	}

	@Override
	public String downloadFileTempUrl(String key) throws Exception {
		return null;
	}


}
