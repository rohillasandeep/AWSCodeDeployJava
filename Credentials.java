package com.amazonaws.samples;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

public class Credentials {

	public static ProfileCredentialsProvider getCredentialsProvider()
	{
		 ProfileCredentialsProvider credentialsProvider = 
				 new ProfileCredentialsProvider("AWS-Dev-19");
	        try {
	            credentialsProvider.getCredentials();
	        } catch (Exception e) {
	            throw new AmazonClientException(
	                    "Cannot load the credentials from the credential profiles file. " +
	                    "Please make sure that your credentials file is at the correct " +
	                    "location (C:\\Users\\srohilla\\.aws\\credentials), and is in valid format.",
	                    e);
	        }
			return credentialsProvider;
	}
}
