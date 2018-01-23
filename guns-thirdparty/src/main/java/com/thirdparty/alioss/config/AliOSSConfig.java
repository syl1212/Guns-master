/**
 * 
 */
package com.thirdparty.alioss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 阿里oos相关配置信息
 * @author yuanli.sun
 *
 */
@Configuration
@PropertySource(value = "classpath:config/alioss.yml",encoding = "utf-8")
public class AliOSSConfig {

	@Value("${alioss.accessKeyId}")
	private String accessKeyId;

	@Value("${alioss.accessKeySecret}")
	private String accessKeySecret;

	@Value("${alioss.endPoint}")
	private String endPoint;

	@Value("${alioss.bucketName}")
	private String bucketName;

	@Value("${alioss.endPointOuternal}")
	private String endPointOuternal;

	@Value("${alioss.compressQualityRate}")
	private String compressQualityRate;

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getEndPointOuternal() {
		return endPointOuternal;
	}

	public void setEndPointOuternal(String endPointOuternal) {
		this.endPointOuternal = endPointOuternal;
	}

	public String getCompressQualityRate() {
		return compressQualityRate;
	}

	public void setCompressQualityRate(String compressQualityRate) {
		this.compressQualityRate = compressQualityRate;
	}
}
