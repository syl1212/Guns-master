/**
 * 
 */
package com.thirdparty.alioss.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.thirdparty.alioss.UploadFilePortal;
import com.thirdparty.alioss.config.AliOSSConfig;
import com.thirdparty.alioss.enu.UploadFileResultEnum;
import com.thirdparty.alioss.exception.GetFileInfoFailedException;
import com.thirdparty.alioss.exception.UploadFileException;
import com.thirdparty.alioss.util.FileUtil;
import com.thirdparty.alioss.util.UploadFileUtils;
import com.thirdparty.alioss.vo.UploadFileDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

/**
 * 通过阿里云oos服务对文件存储
 * @author yuanli.sun
 *
 */
@Service
public class AliOSSFileServiceImpl implements FileRemoteService {

	private static Logger logger= Logger.getLogger(AliOSSFileServiceImpl.class);

    /**
     * 线程执行server
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(10);

	private FileUtil fileUtil = new FileUtil();

	@Autowired
	AliOSSConfig config;

	/* (non-Javadoc)
	 * @see com.health.alioos.service.FileRemoteService#uploadFile(com.health.alioos.vo.UploadDTO)
	 */
	@Override
	public String uploadFile(UploadFileDTO uploadDTO) throws UploadFileException {
		if(StringUtils.isBlank(uploadDTO.getFileName())){
			throw new UploadFileException("upload fileName is empty");
		}
		
		if(StringUtils.indexOf(uploadDTO.getFileName(), ".") == -1){
			throw new UploadFileException("upload fileName ["+uploadDTO.getFileName()+"] is invalid");
		}
		
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(uploadDTO.getFileLength());

			OSSClient ossClient = createOSSClient();
			PutObjectResult result = ossClient.putObject(
					config.getBucketName(), uploadDTO.getFileName(),
					new ByteArrayInputStream(uploadDTO.getInput()), metadata);
			
			
			String url =  this.getAliOOSFileAccessUrl(uploadDTO);
			ossClient.shutdown();

			logger.info("上传文件的名称为：{"+uploadDTO.getFileName()+"}，OSS返回文件ETag为：{"+result.getETag()+"}");
			
			return url;
		} catch (Exception e) {
			logger.error("上传文件失败", e);
			throw new UploadFileException("上传文件["+uploadDTO.getFileName()+"]失败",e);
		}
	}

	/* (non-Javadoc)
	 * @see com.health.alioos.service.FileRemoteService#downloadFile(java.lang.String)
	 */
	@Override
	public byte[] downloadFile(String fileName)
			throws GetFileInfoFailedException {
		byte[] bytes = new byte[0];
		OSSObject obj = null;
		InputStream is = null;
		OSSClient ossClient = null;
		try{
			ossClient = createOSSClient();
			obj = ossClient.getObject(config.getBucketName(),
					fileName);

			is = obj.getObjectContent();
			bytes = IOUtils.readStreamAsByteArray(is);
		} catch (Exception e) {
			this.exception(e);
		} finally {
			IOUtils.safeClose(is);
			if(ossClient != null) {
				ossClient.shutdown();
			}
		}

		return bytes;
	}

    /* (non-Javadoc)
     * @see com.health.alioos.service.FileRemoteService#downloadFile(java.lang.String)
     */
    @Override
	public String downloadFileTempUrl(String key)
            throws GetFileInfoFailedException {
        URL url = null;
        try {
            // 设置URL过期时间为1小时
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
            // 生成URL
			OSSClient ossClient = createOSSClient();
            url = ossClient.generatePresignedUrl(config.getBucketName(), key, expiration);
			ossClient.shutdown();
            return url.toString();
        } catch (Exception e) {
            this.exception(e);
        }
        return "";
    }

	/* (non-Javadoc)
	 * @see com.health.alioos.service.FileRemoteService#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String fileName) throws GetFileInfoFailedException {
		try {
			OSSClient ossClient = createOSSClient();
			ossClient.getObjectMetadata(config.getBucketName(),
						fileName);
			ossClient.shutdown();
		} catch (Exception e) {
			if(e instanceof OSSException){
				OSSException oe = (OSSException) e;
				if("NoSuchKey".equalsIgnoreCase(oe.getErrorCode())) {
					logger.error("" + oe.getErrorCode());
					return false;
				}else{
					exception(e);
				}
			}else{
				exception(e);
			}
		} 
		
		return true;
	}
	
	private OSSClient createOSSClient(){
		return new OSSClient(
				config.getEndPoint(),
				config.getAccessKeyId(),
				config.getAccessKeySecret());
	}
	
	private void exception(Exception e) throws GetFileInfoFailedException {
		e.printStackTrace();
		logger.error("从OSS上获取文件信息失败！", e);
		throw new GetFileInfoFailedException();
	}
	
	private String getAliOOSFileAccessUrl(UploadFileDTO uploadDTO) {
		StringBuffer url = new StringBuffer();
		String outEndPoint = config.getEndPointOuternal();
		String indexStr = getPreUrl(outEndPoint);
		//http://或https://
		url.append(outEndPoint.substring(0, indexStr.length()));
		//桶名
		url.append(config.getBucketName()).append(".");
		//oss-cn-qingdao.aliyuncs.com
		url.append(outEndPoint.substring(indexStr.length()));
		url.append("/");
		
		//文件名
		String lastFileName = uploadDTO.getFileName();
//		if(lastFileName.indexOf(".") > -1) {
//			lastFileName = lastFileName.substring(0, lastFileName.indexOf("."));
//		}
		url.append(lastFileName);
		if(StringUtils.isNotBlank(lastFileName) && lastFileName.indexOf(".") == -1) {
			url.append(uploadDTO.getFileSuffix());
		}
		
		return url.toString();
	}
	
	private String getPreUrl(String url) {
		if(StringUtils.isBlank(url)) {
			return url;
		}
		if(!url.startsWith("http")) {
			return url;
		}
		String indexFlag = "//";
		if(url.indexOf(indexFlag) == -1) {
			return url;
		}
		return url.substring(0, url.indexOf(indexFlag) + indexFlag.length());
	}
	
	/**
	 * 获取最后面部分的文件名
	 * @param fileName
	 * @return
	 */
	private String getLastFileName(String fileName) {
		return fileUtil.getLastFileName(fileName);
	}
	
	/**
     * 处理一个上传的文件
     * @param item
     * @param fileTypeList
     * @return
     */
    public String handleOneFileItem(MultipartFile item, String[] fileTypeList) throws Exception {
    	if(item.isEmpty()){
    		return null;
    	}
    	
    	//获取文件名
    	String name = item.getOriginalFilename();
    	if(StringUtils.isBlank(name)) {
    		return null;
    	}
    	String extName = "";
    	// 得到文件的扩展名
		if (name.lastIndexOf(".") >= 0) {
			extName = name.substring(name.lastIndexOf("."));
		}

		if(fileTypeList.length > 0){
			if (!Arrays.asList(fileTypeList).contains(extName.toLowerCase())) {
				throw new UploadFileException(UploadFileResultEnum.ILLEGAL_FILE_TYPE.getMessage());
			}
		}

		String[] pathArray = null;
		String newFileName = null;
		try {
			//获取pdf文件存储、查看相关路径
			if (".pdf".equalsIgnoreCase(extName)) {
				pathArray = UploadFileUtils.getStaticFilesPdfPath(name);
			} 
			//获取图片存储、查看相关路径
			else {
				pathArray = UploadFileUtils.getStaticFilesImgPath(name);
			}
			newFileName = fileUtil.getLastFileName(pathArray[0]);
			
			//上传文件
			String savePath = pathArray[0];
			
			//获取文件
			byte[] data = item.getBytes();
			if(fileUtil.isImage(extName)) {
				data = fileUtil.compressFileToBytes(item, config.getCompressQualityRate());
			}
			
			//上传成功之后返回文件访问的url
			UploadFileDTO dto = UploadFilePortal.uploadFile(data, savePath, extName);
			//这里从工厂类中获取fileRemoteService不成功，修改为直接service注入调用
			String result = this.uploadFile(dto);
			if(StringUtils.isNotBlank(result) && result.toLowerCase().startsWith("http")) {
				pathArray[1] = result;
			}
		} catch (Exception e) {
			logger.error(String.format("upload file [{}] store to new file name [{}] occure exception ", name,newFileName) ,e);
			if(pathArray != null) {
				logger.info(String.format("upload file [{}] ready store path [{}], access path [{}] ", name, pathArray[0], pathArray[1]));
			}
			throw new UploadFileException(UploadFileResultEnum.UPLOAD_ERROR.getMessage());
		}
		//写入文件地址,访问文件地址,上传文件名,写入文件名
		return pathArray[1];
    }

    /**
     * 处理多文件上传多线程
     * @param files
     * @param fileTypeList
     * @return
     * @throws Exception
     */
    public Map<String, Future<String>> handleMulFilesAsyn(MultipartFile[] files, String[] fileTypeList) throws Exception{
        Map<String, Future<String>> futures = new HashMap<String, Future<String>>();
        try{
            if(files!= null && files.length > 0){
                ExecutorCompletionService pool = new ExecutorCompletionService(executor);
                final int size=files.length;
                final CountDownLatch latch=new CountDownLatch(size);
                for(int i=0;i<files.length;i++){
                    MultipartFile file = files[i];
                    Future<String> f = pool.submit(new Callable<String>() {
                        @Override
						public String call(){
                            try {
                                return handleOneFileItem(file, fileTypeList);
                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.info(String.format("上传文件失败，文件名称：{}", file.getOriginalFilename()));
                            } finally{
                                latch.countDown();
                            }
                            return "";
                        }
                    });
                    futures.put(file.getOriginalFilename(), f);
                }
                latch.await();
                logger.info("多文件上传完成");
            }
        } catch (Exception e){
            throw new UploadFileException(UploadFileResultEnum.UPLOAD_ERROR.getMessage());
        }
		return futures;

	}

    /**
     * 处理多文件上传同步
     * @param files
     * @param fileTypeList
     * @return
     * @throws Exception
     */
    public Map<String, String> handleMulFilesSyn(MultipartFile[] files, String[] fileTypeList) throws Exception{
        Map<String, String> futures = new HashMap<String, String>();
        try{
            if(files!= null && files.length > 0){
                for(int i=0;i<files.length;i++){
                    MultipartFile file = files[i];
                    String name = file.getOriginalFilename();
                    futures.put(name, handleOneFileItem(file, fileTypeList));
                }
                logger.info("多文件上传完成");
            }
        } catch (Exception e){
            throw new UploadFileException(UploadFileResultEnum.UPLOAD_ERROR.getMessage());
        }
        return futures;
    }

}
