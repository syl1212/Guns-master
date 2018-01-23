/**
 * 
 */
package com.thirdparty.alioss.service;

import com.thirdparty.alioss.exception.GetFileInfoFailedException;
import com.thirdparty.alioss.exception.UploadFileException;
import com.thirdparty.alioss.vo.UploadFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 远程文件存储服务接口
 * @author yuanli.sun
 *
 */
public interface FileRemoteService {
	/**
	 * 上传文件
	 * 
	 * @param uploadDTO
	 * @throws UploadFileException
	 */
	String uploadFile(UploadFileDTO uploadDTO) throws UploadFileException;

	/**
	 * 根据文件名称，获取文件流
	 * 
	 * @param fileName
	 * @return 文件字节数组
	 */
	byte[] downloadFile(String fileName) throws GetFileInfoFailedException;

	/**
	 * 文件是否存在
	 * @param fileName
	 * @return
	 */
	boolean exists(String fileName)  throws GetFileInfoFailedException ;

	/**
	 * 根据文件名称，获取文件流
	 *
	 * @param fileName
	 * @return 文件字节数组
	 */
	String downloadFileTempUrl(String key) throws Exception ;

}
