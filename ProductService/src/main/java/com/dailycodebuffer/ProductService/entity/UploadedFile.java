package com.dailycodebuffer.ProductService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "uploaded_files")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadedFile {

    @Id
    private String id;
    private String filename;
    private String contentType;
    private Object content;
    private String raw;
    private LocalDateTime uploadTime;
}
