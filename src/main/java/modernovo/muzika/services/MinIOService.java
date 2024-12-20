package modernovo.muzika.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Retention;
import io.minio.messages.RetentionMode;
import modernovo.muzika.services.dto.creators.MinIOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;


@Service
public class MinIOService {

    private final MinioClient minioClient;

    @Value("${minio.root.bucket}")
    private String rootBucket;

    public MinIOService(@Value("${minio.url}") String url,
                        @Value("${minio.port}") int port, @Value("${minio.root.user}") String user, @Value("${minio.root.password}") String password) {
        minioClient = MinioClient.builder().endpoint(url, port, false).credentials(user, password).build();
    }

    public void saveAndLockFile(InputStream inputStream, long fileSize, String fileName) {
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(rootBucket).object(fileName)
                    .stream(inputStream, fileSize, -1)
                    .retention(new Retention(RetentionMode.COMPLIANCE, ZonedDateTime.now().plusDays(1))).build());
        } catch (Exception e) {
            throw new MinIOException("Failed to save and lock file:" + e.getMessage(), e);
        }
    }

    public void unlockFile(String fileName) {
        try {
            minioClient.setObjectRetention(
                    SetObjectRetentionArgs.builder()
                            .bucket(rootBucket)
                            .object(fileName)
                            .config(new Retention(RetentionMode.GOVERNANCE, ZonedDateTime.now().minusDays(1)))
                            .bypassGovernanceMode(true)
                            .build());
        } catch (Exception e) {
            throw new MinIOException("Failed to unlock file:" + e.getMessage(), e);
        }
    }

    public void removeFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(rootBucket).object(fileName).build());
        } catch (Exception e) {
            throw new MinIOException("Failed to remove file:" + e.getMessage(), e);
        }
    }

    public InputStream getFile(String fileName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(rootBucket).object(fileName).build());
        } catch (Exception e) {
            throw new MinIOException("Failed to get file:" + e.getMessage(), e);
        }
    }


}
