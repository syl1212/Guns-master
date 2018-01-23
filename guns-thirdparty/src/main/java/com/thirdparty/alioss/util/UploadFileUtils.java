package com.thirdparty.alioss.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

public class UploadFileUtils {
	private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);
	public static String FILE_PRE_FIX = "200";
	
	/**
	 * 获取文件路径，string[0]物理路径，string[1]http路径
	 * @param fileOrgName
	 * @return
	 */
	public static String[] getStaticFilesImgPath(String fileOrgName) {
		
		return getFilePath("guns-alioss", "images", fileOrgName);
	}
	
	public static String[] getStaticFilesPdfPath(String fileOrgName) {
		return getFilePath("guns-alioss", "pfd", fileOrgName);
	}
	
	public static String[] getFilePath(String fileRoot, String dir,
										String fileOrgName) {
		String[] pathArray = new String[2];
		
		String currDate = DateUtils.date2Str(new Date(), "yyyyMMdd");
		String extName = fileOrgName.substring(fileOrgName.lastIndexOf('.') + 1);
		
		String filePath = fileRoot + "/" + dir + "/" + currDate + "/" + FILE_PRE_FIX + "_" + Sequence.uniqId() + "." + extName;
		pathArray[0] = filePath;
		
		
		return pathArray;
	}
	
}
