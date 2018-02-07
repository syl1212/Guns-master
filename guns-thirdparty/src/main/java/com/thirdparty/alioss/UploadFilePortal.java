/**
 * 
 */
package com.thirdparty.alioss;

import com.thirdparty.alioss.exception.UploadFileException;
import com.thirdparty.alioss.vo.UploadFileDTO;
import org.apache.commons.fileupload.FileItem;

/**
 * 文件上传入口
 * @author = yuanli.sun
 *
 */
public class UploadFilePortal {
	
	public static UploadFileDTO uploadFile(byte[] data, String storeFilePath, String suffix) throws UploadFileException  {
		UploadFileDTO uploadDTO = new UploadFileDTO();
		uploadDTO.setFileLength(Long.parseLong(String.valueOf(data.length)));
		uploadDTO.setInput(data);
		uploadDTO.setFileStorePathName(storeFilePath);
		uploadDTO.setFileSuffix(suffix);
		uploadDTO.setFileName(storeFilePath);
		
		return uploadDTO;
	}
	
	public static UploadFileDTO uploadFile(byte[] data, String storeFilePath) throws UploadFileException  {
		UploadFileDTO uploadDTO = new UploadFileDTO();
		uploadDTO.setFileLength(Long.parseLong(String.valueOf(data.length)));
		uploadDTO.setInput(data);
		uploadDTO.setFileStorePathName(storeFilePath);
		uploadDTO.setFileName(storeFilePath);
		
		return uploadDTO;
	}
	
	/**
	 * 文件上传操作
	 * @param item
	 * @param storeFilePath
	 * @return
	 * @throws UploadFileException
	 */
	public static UploadFileDTO uploadFile(FileItem item, String storeFilePath) throws UploadFileException  {
		UploadFileDTO uploadDTO = new UploadFileDTO();
		uploadDTO.setFileLength(item.getSize());
		uploadDTO.setInput(item.get());
		uploadDTO.setFileStorePathName(storeFilePath);
		uploadDTO.setFileName(storeFilePath);
		
		return uploadDTO;
	}
	
}
