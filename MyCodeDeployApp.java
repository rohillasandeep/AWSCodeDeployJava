package com.amazonaws.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.services.codedeploy.AmazonCodeDeployClientBuilder;
import com.amazonaws.services.codedeploy.model.CreateDeploymentRequest;
import com.amazonaws.services.codedeploy.model.RevisionLocation;
import com.amazonaws.services.codedeploy.model.S3Location;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;



public class MyCodeDeployApp {

	static String  c_BucketName="";
	static String c_BucketObjectPath="";
	static String c_Region="";
	static String c_CDApplicationName="";
	static String c_CDGroupName="";
	static String c_ConfigFile="";
	static String c_ConfigFileProfileName="";
	static String c_BucketPrefix="";
	static String c_BuildFilePath="";
	static String c_FileExistsBehavior="";
	static String c_DeploymentConfigName="";
	static boolean isDebug=false;
	
	public static void main(String[] args) throws Exception {
		
		// Reading configuration file
		readConfiguration(args[0].toString());		
		long startTime = System.currentTimeMillis();
		try {
		if(args.length>1)
		{
			if("debug=true".equalsIgnoreCase(args[1].toString().trim()))
			{
				isDebug=true;
			}
		}
		}catch (Exception E)
		{
			
		}
		
		//Fetching AWS Credentials
		ProfilesConfigFile pcf = new ProfilesConfigFile(c_ConfigFile);
		debug(isDebug, "Fetching AWS Credentials");
		
		ProfileCredentialsProvider credentialsProvider
         = new ProfileCredentialsProvider(pcf,c_ConfigFileProfileName);
		
		//ProfileCredentialsProvider credentialsProvider = Credentials.getCredentialsProvider();
		
		//Creating S3 Client
		debug(isDebug, "Creating S3 Client");
		AmazonS3 s3 = AmazonS3ClientBuilder.standard()
		            .withCredentials(credentialsProvider)
		            .withRegion(c_Region)
		            .build();
		
		debug(isDebug, "Creating deplotment file object");
		java.io.File myfile = new File(c_BuildFilePath);
		
		//Deleting existing S3 build object
		debug(isDebug, "Deleting existing S3 build object");
		s3.deleteObject(c_BucketName, c_BucketObjectPath);
		
       // Uploading a new object to S3 from a file
		debug(isDebug, "Uploading a new deployment to S3 from a file");
       s3.putObject(new PutObjectRequest(c_BucketName, c_BucketObjectPath, myfile));
		
       debug(isDebug, "Fetch Code Deploy Object");
		com.amazonaws.services.codedeploy.AmazonCodeDeploy cd = AmazonCodeDeployClientBuilder.standard()
				.withCredentials(credentialsProvider).withRegion(c_Region).build();

		S3Location s3l = S3.getS3location(c_BucketName, c_BucketObjectPath);

		RevisionLocation rl = S3.getRevisionLocation(s3l);
		
		debug(isDebug, "Fetch Code Deploy Application");
		CodeDeploy.getApplicationRequest(c_CDApplicationName, credentialsProvider);

		debug(isDebug, "Create Code Deploy Deployment Object");
		CreateDeploymentRequest cdr = new CreateDeploymentRequest();
		cdr.setApplicationName(c_CDApplicationName);
		cdr.setDeploymentGroupName(c_CDGroupName);
		cdr.setFileExistsBehavior(c_FileExistsBehavior);
		cdr.setDeploymentConfigName(c_DeploymentConfigName);
		cdr.setRevision(rl);
		
		debug(isDebug, "Execute Deployment");
		boolean getDeploymentStatus = CodeDeploy.getDeploymentStatus(cd, cdr, isDebug);
		
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    debug(isDebug, "Time Taken: "+elapsedTime/1000 + " seconds");
		
		debug(isDebug, "Result: ");
		System.out.println(getDeploymentStatus);
		
	}
	
	public static void readConfiguration(String location)
	{
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(location);

			// load a properties file
			prop.load(input);

			c_BucketName=prop.getProperty("BucketName");
			c_BucketObjectPath=prop.getProperty("BucketObjectPath");
			c_Region=prop.getProperty("Region");
			c_CDApplicationName=prop.getProperty("CDApplicationName");
			c_CDGroupName=prop.getProperty("DeploymentGroupName");
			c_ConfigFile=prop.getProperty("CredentialsFile");
			c_ConfigFileProfileName=prop.getProperty("ConfigFileProfileName");
			c_BucketPrefix=prop.getProperty("BucketPrefix");
			c_BuildFilePath=prop.getProperty("BuildFilePath");
			c_FileExistsBehavior=prop.getProperty("FileExistsBehavior");
			c_DeploymentConfigName=prop.getProperty("DeploymentConfigName");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public static void debug(boolean bool, String message)
	{
		if(bool==true)
		{
		System.out.println(message);
		}
	}
	
	public static String readFileAsString(String fileName)throws Exception 
	  { 
	    String data = ""; 
	    data = new String(Files.readAllBytes(Paths.get(fileName))); 
	    return data; 
	  } 

}
