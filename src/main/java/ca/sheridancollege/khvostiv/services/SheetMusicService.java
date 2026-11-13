package ca.sheridancollege.khvostiv.services;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ca.sheridancollege.khvostiv.beans.PianoPiece;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class SheetMusicService {

    private final S3Client s3Client;
    private final String bucketName;

    public SheetMusicService(
            S3Client s3Client,
            @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    public void saveSheetMusic(
            MultipartFile sheetMusicFile,
            PianoPiece pianoPiece) throws IOException {

        if (sheetMusicFile == null || sheetMusicFile.isEmpty()) {
            return;
        }

        String originalFileName = sheetMusicFile.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("The PDF file must have a name.");
        }

        originalFileName = Paths.get(originalFileName)
                .getFileName()
                .toString();

        if (!originalFileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        String previousObjectKey = pianoPiece.getSheetMusicPath();

        String objectKey = "sheet-music/"
                + UUID.randomUUID()
                + "-"
                + originalFileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType("application/pdf")
                .build();

        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(sheetMusicFile.getBytes()));

            if (previousObjectKey != null
                    && previousObjectKey.startsWith("sheet-music/")) {

                DeleteObjectRequest deleteObjectRequest =
                        DeleteObjectRequest.builder()
                                .bucket(bucketName)
                                .key(previousObjectKey)
                                .build();

                s3Client.deleteObject(deleteObjectRequest);
            }

            pianoPiece.setSheetMusicFileName(originalFileName);
            pianoPiece.setSheetMusicPath(objectKey);

        } catch (S3Exception exception) {
            throw new IOException(
                    "Could not upload the PDF to Amazon S3.",
                    exception);
        }
    }

    public Resource getSheetMusic(PianoPiece pianoPiece) throws IOException {

        String objectKey = pianoPiece.getSheetMusicPath();

        if (objectKey == null || objectKey.isBlank()) {
            throw new IOException("This piano piece has no sheet music.");
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        try {
            ResponseBytes<GetObjectResponse> objectBytes =
                    s3Client.getObjectAsBytes(getObjectRequest);

            return new ByteArrayResource(objectBytes.asByteArray());

        } catch (S3Exception exception) {
            throw new IOException(
                    "Could not retrieve the PDF from Amazon S3.",
                    exception);
        }
    }
    
    public void deleteSheetMusic(PianoPiece pianoPiece) throws IOException {

        String objectKey = pianoPiece.getSheetMusicPath();

        if (objectKey == null
                || !objectKey.startsWith("sheet-music/")) {
            return;
        }

        DeleteObjectRequest deleteObjectRequest =
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build();

        try {
            s3Client.deleteObject(deleteObjectRequest);

        } catch (S3Exception exception) {
            throw new IOException(
                    "Could not delete the PDF from Amazon S3.",
                    exception);
        }
    }
    
}