package life.gerayking.community.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.aliyuncs.ram.model.v20150501.ListAccessKeysResponse;
import life.gerayking.community.community.dto.AccesstokenDTO;
import life.gerayking.community.community.exception.CustomizeErrorCode;
import life.gerayking.community.community.exception.CustomizeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.UUID;

@Component
public class AliyunOssProvider {
    @Value("${aliyun-oss-bucketName}")
    private String bucketName;
    @Value("${aliyun-oss-region}")
    private String region;
    @Value("${aliyun-oss-endpoint}")
    private String endpoint;
    @Value("${aliyun-oss-accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun-oss-accessKeySecret}")
    private String accessKeySecret;
    private String fileHost= "images";
    private static String File_URL;
    private static final Logger logger = LoggerFactory.getLogger(AliyunOssProvider.class);

    public String upload(File file) {
        boolean isImage = true;
        try {
            Image image = ImageIO.read(file);
            isImage = image == null ? false : true;
        } catch (Exception e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        if (file == null || !isImage) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }

        OSS ossClient = new OSSClientBuilder().build(endpoint,
                accessKeyId,
                accessKeySecret);
        try{
            if (!ossClient.doesBucketExist(bucketName)) {

                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            String fileUrl =fileHost + "/"+ UUID.randomUUID().toString()+"."+file.getName();
            if(isImage){
                File_URL ="https://"+bucketName + "." + endpoint + "/" + fileUrl;
            }
            else{
                File_URL = "非图片，不可预览。文件路径为：" + fileUrl;
            }
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileUrl, file);
            ObjectMetadata metadata  = new ObjectMetadata();
            metadata.setObjectAcl(CannedAccessControlList.PublicRead);
            putObjectRequest.setMetadata(metadata);
            ossClient.putObject(putObjectRequest);
        }catch (Exception e){
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }finally {
            if(ossClient!=null){
                ossClient.shutdown();
            }
        }
        return File_URL;
    }
}
