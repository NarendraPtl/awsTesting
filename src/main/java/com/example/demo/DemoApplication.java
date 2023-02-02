package com.example.demo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.GetAccountAuthorizationDetailsResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.*;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        //ListUsers(args);
        comDependency(args);
    }
    public static void ListUsers(String[] args) {
        AwsCredentialsProvider var1 = new AwsCredentialsProvider() {
            @Override
            public AwsCredentials resolveCredentials() {
                return new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return "AKIA5373Z7TDFLQ5WNHD";
                    }

                    @Override
                    public String secretAccessKey() {
                        return "oBieCrjt2lxh+GRY53eVRVnyYpCc1XSmZmNdmVr+";
                    }
                };
            }
        };
        Region region = Region.AWS_GLOBAL;
        IamClient iam = IamClient.builder()
                .region(region)
                .credentialsProvider(var1)
                .build();

        listAllUsers(iam);
        System.out.println("Done");
        iam.close();
    }

    public static void listAllUsers(IamClient iam) {

        //ObjectMapper obj = new ObjectMapper();
        try {

            boolean done = false;
            String newMarker = null;
            GetAccountAuthorizationDetailsResponse response = null;
            GetAccountSummaryResponse getAccountSummaryResponse = null;
            GetAccountPasswordPolicyResponse getAccountPasswordPolicyResponse = null;

            while (!done) {
                if (newMarker == null) {
                    GetAccountAuthorizationDetailsRequest request = GetAccountAuthorizationDetailsRequest.builder().build();
                    response = iam.getAccountAuthorizationDetails(request);

                } else {
                    GetAccountAuthorizationDetailsRequest request = GetAccountAuthorizationDetailsRequest.builder()
                            .marker(newMarker)
                            .build();
                    response = iam.getAccountAuthorizationDetails(request);
                }

                if (!response.isTruncated()) {
                    done = true;
                } else {
                    newMarker = response.marker();
                }
            }

           // getAccountPasswordPolicyResponse = iam.getAccountPasswordPolicy();
            getAccountSummaryResponse = iam.getAccountSummary();

            //String result = obj.writeValueAsString(response);

            System.out.format("\n Retrieved user  userDetailList %s", response.userDetailList());


           // System.out.format("\n Retrieved user  groupDetailList %s", response.groupDetailList());

            //System.out.format("\n Retrieved user  roleDetailList %s", response.roleDetailList());

            //System.out.format("\n Retrieved user  policies %s", response.policies());

            //System.out.format("\n Retrieved user  getAccountPasswordPolicyResponse %s", getAccountPasswordPolicyResponse);

            //System.out.format("\n Retrieved user  getAccountSummaryResponse %s", getAccountSummaryResponse);


        } catch (IamException e) {
            System.err.println(e.fillInStackTrace());
            System.exit(1);
        }
    }


    public static void comDependency(String[] args) {
        ObjectMapper obj = new ObjectMapper();
        AmazonIdentityManagement client = AmazonIdentityManagementClientBuilder.standard()
                .withRegion(Regions.DEFAULT_REGION).withCredentials(
                        new AWSStaticCredentialsProvider( new BasicAWSCredentials("AKIA5373Z7TDFLQ5WNHD", "oBieCrjt2lxh+GRY53eVRVnyYpCc1XSmZmNdmVr+")
                )).build();

        GetAccountAuthorizationDetailsResult getAccountAuthorizationDetailsResult = client.getAccountAuthorizationDetails();

       // System.out.format("\n Retrieved user  userDetailList %s", client.getCredentialReport());

        System.out.format("\n getAccountAuthorizationDetailsResult  userDetailList %s", getAccountAuthorizationDetailsResult.getUserDetailList());

       // System.out.format("\n getAccountAuthorizationDetailsResult  groupDetailList %s", getAccountAuthorizationDetailsResult.getGroupDetailList());

       // System.out.format("\n getAccountAuthorizationDetailsResult  roleDetailList %s", getAccountAuthorizationDetailsResult.getRoleDetailList());

       // System.out.format("\n getAccountAuthorizationDetailsResult  policies %s", getAccountAuthorizationDetailsResult.getPolicies());

    }
}