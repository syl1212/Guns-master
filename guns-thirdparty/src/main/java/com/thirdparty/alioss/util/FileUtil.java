package com.thirdparty.alioss.util;

import com.aliyun.oss.common.utils.IOUtils;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifImageDirectory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.thirdparty.alioss.config.AliOSSConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public byte[] getNetworkImageDatas(String imageUrl)
			throws IOException {
		byte[] data = null;
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			InputStream inStream = conn.getInputStream();
			data = readInputStream(inStream);
		} catch (Exception e) {
			throw new IOException("read imageUrl occure exception", e);
		}
		return data;
	}

	public byte[] readInputStream(InputStream inStream)
			throws IOException {
		return IOUtils.readStreamAsByteArray(inStream);
	}

	/**
	 * 从完整的文件路径获取最后面部分的文件名
	 * 
	 * @param fileName
	 * @return
	 */
	public String getLastFileName(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return fileName;
		}
		if (fileName.indexOf("/") == -1) {
			return fileName;
		}
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

		if (fileName.indexOf("\\") == -1) {
			return fileName;
		}
		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);

		return fileName;
	}

	/**
	 * 判断是否是图片格式
	 * 
	 * @param suffix
	 * @return
	 */
	public boolean isImage(String suffix) {
		if (".jpg".equalsIgnoreCase(suffix) || ".bmp".equalsIgnoreCase(suffix)
				|| ".png".equalsIgnoreCase(suffix)) {
			return true;
		}
		return false;
	}

	public byte[] bufferedImageToBytes(BufferedImage image, float quality)
			throws IOException {
		if (image == null) {
			return null;
		}
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();

		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(quality);
		iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

		ColorModel colorModel = ColorModel.getRGBdefault();
		iwp.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel,
				colorModel.createCompatibleSampleModel(16, 16)));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		IIOImage iIamge = new IIOImage(image, null, null);

		writer.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
		writer.write(null, iIamge, iwp);

		return byteArrayOutputStream.toByteArray();
	}

	public byte[] compressFileToBytes(String filePath, float quality)
			throws IOException {
		BufferedImage image = ImageIO.read(new File(filePath));
		return bufferedImageToBytes(image, quality);
	}

	// public static byte[] compressFileToBytes(String filePath, float quality)
	// throws IOException {
	// // BufferedImage image = ImageIO.read(new File(filePath));
	// // int newWidth = (int) (((double) image.getWidth(null)) / quality);
	// // int newHeight = (int) (((double) image.getHeight(null)) / quality);
	// // // return bufferedImageToBytes(image, quality);
	// return compressPic(toByteArray(filePath), 0, 0, quality, true);
	// }

	public byte[] compressFileToBytes(InputStream inputStream, boolean flag, String rate)
			throws IOException {
		//默认不压缩,如果true则压缩
		float quality = 1;
		if(flag){
			quality = Float.parseFloat(rate);
		}
		
		//转输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		byte[] buffer = new byte[1024];  
		int len;  
		while ((len = inputStream.read(buffer)) > -1 ) {  
		    baos.write(buffer, 0, len);  
		}  
		baos.flush();  
		
		BufferedImage image = rotatePhonePhoto(new ByteArrayInputStream(baos.toByteArray()),
				getRotateAngleForPhoto(new ByteArrayInputStream(baos.toByteArray())));
		return bufferedImageToBytes(image, quality);
		// return compressPic(readStream(inputStream), 0, 0, quality, true);
	}

	public byte[] compressFileToBytes(MultipartFile item, String rate)
			throws IOException {
		float quality = Float.parseFloat(rate);
		BufferedImage image = rotatePhonePhoto(item.getInputStream(),
				getRotateAngleForPhoto(item.getInputStream()));
		return bufferedImageToBytes(image, quality);
		// return compressPic(readStream(inputStream), 0, 0, quality, true);
	}

	public byte[] compressFileToBytes(byte[] data, String rate) throws IOException {
		float quality = Float.parseFloat(rate);
		return compressPic(data, 0, 0, quality, true);
		// return bufferedImageToBytes(image, quality);
	}

	public String repayALiOOSUrlToLocalServerUrl(String alOOSUrl, String endPointOuternal) {
		if (StringUtils.isEmpty(alOOSUrl)) {
			return alOOSUrl;
		}

		String outEndPoint = endPointOuternal;
		if (outEndPoint.indexOf("//") > -1) {
			outEndPoint = outEndPoint.substring(outEndPoint.indexOf("//") + 2);
		}
		if (alOOSUrl.indexOf(outEndPoint) == -1) {
			return alOOSUrl;
		}

		StringBuffer url = new StringBuffer();
		// url.append(AppConstantsUtil.getHostHttpUrl()).append("/");
		url.append("upload/search/");

		String lastFileName = getLastFileName(alOOSUrl);
		url.append(getFileName(lastFileName)).append("/");
		String suffix = getFileSuffix(lastFileName);
		if (StringUtils.isEmpty(suffix)) {
			suffix = "jpg";
		}
		url.append(suffix);

		logger.info("repay alioos url {} to local server {}", alOOSUrl, url.toString());

		return url.toString();
	}

	public String getFileName(String fileName) {
		if (StringUtils.isEmpty(fileName)) {
			return fileName;
		}
		if (fileName.indexOf(".") > -1) {
			return fileName.substring(0, fileName.indexOf("."));
		}
		return fileName;
	}

	public String getFileSuffix(String fileName) {
		if (StringUtils.isEmpty(fileName) || fileName.indexOf(".") == -1) {
			return null;
		}
		return fileName.substring(fileName.indexOf(".") + 1);
	}

	public byte[] compressPic(byte[] imageByte, int width, int height,
			double rate, boolean gp) {
		byte[] inByte = null;
		try {
			File file = new File("D:\\test_upload_file\\upload.jpg");
			// 建立输出字节流
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(imageByte);
			System.out.println("写入成功");
			// 为了节省IO流的开销，需要关闭
			fos.close();

			ByteArrayInputStream byteInput = new ByteArrayInputStream(imageByte);
			BufferedImage img = ImageIO.read(byteInput);

			ImageIO.write(img, "jpg", new File(
					"D:\\test_upload_file\\upload_Img_tmp.jpg"));

			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				return inByte;
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (gp == true) {
					// 为等比缩放计算输出的图片宽度及高度
					// double rate1 = ((double) img.getWidth(null)) / (double)
					// width + 0.1;
					// double rate2 = ((double) img.getHeight(null))/ (double)
					// height + 0.1;
					// 根据缩放比率大的进行缩放控制
					// double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) * rate);
					newHeight = (int) (((double) img.getHeight(null)) * rate);
				} else {
					newWidth = width; // 输出的图片宽度
					newHeight = height; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage((int) newWidth,
						(int) newHeight, BufferedImage.TYPE_INT_RGB);
				img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				tag.getGraphics().drawImage(
						img.getScaledInstance(newWidth, newHeight,
								Image.SCALE_SMOOTH), 0, 0, null);

				ImageWriter imgWrier;
				ImageWriteParam imgWriteParams;
				// 指定写图片的方式为 jpg
				imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
				imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
						null);
				// // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
				// imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
				// // 这里指定压缩的程度，参数qality是取值0~1范围内，
				// imgWriteParams.setCompressionQuality((float)45217/imageByte.length);
				//
				// imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
				// ColorModel colorModel = ColorModel.getRGBdefault();
				// // 指定压缩时使用的色彩模式
				// imgWriteParams.setDestinationType(new
				// javax.imageio.ImageTypeSpecifier(colorModel, colorModel
				// .createCompatibleSampleModel(100, 100)));
				// 将压缩后的图片返回字节流
				ByteArrayOutputStream out = new ByteArrayOutputStream(
						imageByte.length);
				imgWrier.reset();
				// 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
				// OutputStream构造
				imgWrier.setOutput(ImageIO.createImageOutputStream(out));
				// 调用write方法，就可以向输入流写图片
				imgWrier.write(null, new IIOImage(tag, null, null),
						imgWriteParams);
				out.flush();
				out.close();
				byteInput.close();
				inByte = out.toByteArray();

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return inByte;
	}

	public byte[] toByteArray(String filename) throws IOException {

		File f = new File(filename);
		if (!f.exists()) {
			throw new FileNotFoundException(filename);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	public byte[] readStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 获取图片正确显示需要旋转的角度（顺时针）
	 * 
	 * @return
	 */
	public int getRotateAngleForPhoto(InputStream inputStream) {
		int angle = 0;
		Metadata metadata;
		try {
			metadata = JpegMetadataReader.readMetadata(inputStream);
			Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
			if (directory != null && directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION)) {

				// Exif信息中方向　　
				int orientation = directory
						.getInt(ExifDirectoryBase.TAG_ORIENTATION);
				// 原图片的方向信息
				if (6 == orientation) {
					// 6旋转90
					angle = 90;
				} else if (3 == orientation) {
					// 3旋转180
					angle = 180;
				} else if (8 == orientation) {
					// 8旋转90
					angle = 270;
				}
			}
		} catch (JpegProcessingException e) {
			e.printStackTrace();
		} catch (MetadataException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}

		return angle;
	}

	/**
	 * 旋转手机照片
	 * 
	 * @return
	 */
	public BufferedImage rotatePhonePhoto(InputStream inputStream,
			int angel) {
		BufferedImage src = null;
		BufferedImage res = null;
		try {
			src = ImageIO.read(inputStream);
			int src_width = src.getWidth(null);
			int src_height = src.getHeight(null);
			Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(
					src_width, src_height)), angel);
			res = new BufferedImage(rect_des.width, rect_des.height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = res.createGraphics();
			g2.translate((rect_des.width - src_width) / 2,
					(rect_des.height - src_height) / 2);
			g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
			g2.drawImage(src, null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public Rectangle CalcRotatedSize(Rectangle src, int angel) {
		// if angel is greater than 90 degree, we need to do some conversion
		if (angel >= 90) {
			if (angel / 90 % 2 == 1) {
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}

		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);

		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
				- angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
				- angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;
		return new Rectangle(new Dimension(des_width, des_height));
	}
}
