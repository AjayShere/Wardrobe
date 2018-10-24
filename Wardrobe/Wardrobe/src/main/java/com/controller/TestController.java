package com.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@RestController
public class TestController {
	
	
	
	@Autowired
	public AmazonS3  s3Client; 
	
	@Value("${filepath}")
	private String path;
	
	@Value("${bucketname}")
	private String bucketName;
	
	@Value("${S3KeyName}")
	private String keyName;
	
	@Value("${endpointUrl}")
	private String endpointUrl;
	
	
	
	
	@RequestMapping(value="/test" , method = RequestMethod.GET)
	public String testMethod(){
		
		
		Path path = Paths.get("C:\\File\\MyDoc.txt");
		String name = "file.txt";
		String originalFileName = "file.txt";
		String contentType = "text/plain";
		byte[] content = null;
		try {
			System.out.println("reading file data");
		    content = Files.readAllBytes(path);
		} catch (final IOException e) {
		}
		MultipartFile result = new MockMultipartFile(name,
		                     originalFileName, contentType, content);

		this.uploadFile(result);
		
		/*try{
			//Path path = Paths.get("C:\\File\\MyDoc.txt");
		File file = new File("F:\\File\\MyDoc.txt");
		s3Client.putObject(new PutObjectRequest(bucketName, keyName, file));
		
		}catch(AmazonServiceException  ase){
			System.out.println("exception" +ase);
		}*/
		
		return "Success";
		
		
	}
	
	public void uploadFile(MultipartFile multipartFile){
		
		String fileUrl ="";
		
		try {
	        File file = convertMultiPartToFile(multipartFile);
	        String fileName = generateFileName(multipartFile);
	        fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
	        
	        System.out.println("File URL ---------"  +fileUrl);
	        uploadFileTos3bucket(fileName, file);
	        file.delete();
	    } catch (Exception e) {
	       e.printStackTrace();
	    }
		
		
	}
	
	private File convertMultiPartToFile(MultipartFile file) throws IOException{
		
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
		
	}
	
	private String generateFileName(MultipartFile multiPart) {
	    return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}
	
	private void uploadFileTos3bucket(String fileName, File file) {
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
	            .withCannedAcl(CannedAccessControlList.PublicRead));
	}
	
	
}
