package com.mjuAppSW.appName.domain.member.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageRequest {
    private Long id;
    private String base64Picture;
}
