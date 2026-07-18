package com.sudarshan.wealth.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sudarshan.wealth.service.S3Service;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        return ResponseEntity.ok(
                s3Service.uploadFile(file));
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable String fileName) {

        InputStream inputStream = s3Service.downloadFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }
    
    @GetMapping("/presigned/{fileName}")
    public ResponseEntity<String> generateUrl(
            @PathVariable String fileName) {

        return ResponseEntity.ok(
                s3Service.generatePreSignedUrl(fileName));
    }
}