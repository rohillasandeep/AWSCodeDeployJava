package com.amazonaws.samples;

import java.util.concurrent.TimeUnit;

import com.amazonaws.services.codedeploy.model.CreateDeploymentRequest;
import com.amazonaws.services.codedeploy.model.CreateDeploymentResult;
import com.amazonaws.services.codedeploy.model.DeploymentInfo;
import com.amazonaws.services.codedeploy.model.DeploymentStatus;
import com.amazonaws.services.codedeploy.model.FileExistsBehavior;
import com.amazonaws.services.codedeploy.model.GetApplicationRequest;
import com.amazonaws.services.codedeploy.model.GetDeploymentTargetRequest;

public class CodeDeploy {

	public static com.amazonaws.services.codedeploy.model.GetApplicationRequest getApplicationRequest(String name,
			com.amazonaws.auth.profile.ProfileCredentialsProvider pr) {
		com.amazonaws.services.codedeploy.model.GetApplicationRequest ar = new GetApplicationRequest();
		ar.setApplicationName(name);
		ar.withApplicationName(name);
		ar.withRequestCredentialsProvider(pr);
		return ar;
	}

	public static boolean getDeploymentStatus(com.amazonaws.services.codedeploy.AmazonCodeDeploy cd,
			CreateDeploymentRequest cdr, boolean isDebug) throws InterruptedException {
		CreateDeploymentResult cdrs = cd.createDeployment(cdr);
		
		com.amazonaws.services.codedeploy.model.GetDeploymentRequest gdr = new com.amazonaws.services.codedeploy.model.GetDeploymentRequest();

		cdr.setFileExistsBehavior(FileExistsBehavior.OVERWRITE);
		
		gdr.setDeploymentId(cdrs.getDeploymentId());
		
		//String status = cd.getDeployment(gdr).getDeploymentInfo().getStatus();

		boolean success = false;

		while (true) {
		TimeUnit.MILLISECONDS.sleep(1000);
		MyCodeDeployApp.debug(isDebug, "Deployment status : "+cd.getDeployment(gdr).getDeploymentInfo().getStatus().toString());
			if (cd.getDeployment(gdr).getDeploymentInfo().getStatus().toString().equalsIgnoreCase("Succeeded")
					|| cd.getDeployment(gdr).getDeploymentInfo().getStatus().toString().equalsIgnoreCase("Failed")
					|| cd.getDeployment(gdr).getDeploymentInfo().getStatus().toString().equalsIgnoreCase("Stopped")) {
				if (cd.getDeployment(gdr).getDeploymentInfo().getStatus().toString().equalsIgnoreCase("Succeeded")) {
					success = true;
				}
				if (cd.getDeployment(gdr).getDeploymentInfo().getStatus().toString().equalsIgnoreCase("Failed")) {

					MyCodeDeployApp.debug(isDebug, "Error: "+cd.getDeployment(gdr).getDeploymentInfo().getErrorInformation().getCode()+" : "+cd.getDeployment(gdr).getDeploymentInfo().getErrorInformation()+"\n");
				}
				break;
			}
		}

		return success;

	}

}
