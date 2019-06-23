package com.amazonaws.samples;

import com.amazonaws.services.codedeploy.model.BundleType;
import com.amazonaws.services.codedeploy.model.RevisionLocation;
import com.amazonaws.services.codedeploy.model.RevisionLocationType;
import com.amazonaws.services.codedeploy.model.S3Location;

public class S3 {

	public static S3Location getS3location(String mybucketname, String mykeyname) {
		S3Location s3l = new S3Location();
		s3l.setBucket(mybucketname);
		s3l.setKey(mykeyname);
		s3l.setBundleType(BundleType.Zip);
		return s3l;
	}

	public static RevisionLocation getRevisionLocation(S3Location s3l) {
		RevisionLocation rl = new RevisionLocation();

		rl.setS3Location(s3l);
		rl.setRevisionType(RevisionLocationType.S3);

		return rl;
	}
}
