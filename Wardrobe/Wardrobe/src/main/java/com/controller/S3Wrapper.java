package com.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


@Configuration
public class S3Wrapper {
	
	
	@Value("${accesskey}")
	public String accessKey;
	
	@Value("${secretkry}")
	public String secretkey;
	
	@Value("${bucketname}")
	public String bucketName;
	
	
	@Value("${region}")
	public String region;
	
	@Bean
	public AmazonS3 s3Client(){
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretkey);
		
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
								.withRegion(Regions.fromName(region))
		                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
		                        .build();
		
		return s3Client;
	}
	
	
	

}
