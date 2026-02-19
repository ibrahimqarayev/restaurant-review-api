package com.ibrahim.restaurant.controller;

import com.ibrahim.restaurant.dto.photo.PhotoDto;
import com.ibrahim.restaurant.entity.Photo;
import com.ibrahim.restaurant.mapper.PhotoMapper;
import com.ibrahim.restaurant.service.PhotoService;
import com.ibrahim.restaurant.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    @PostMapping
    public ResponseEntity<PhotoDto> uploadPhoto(@RequestParam("file") MultipartFile file) {
        Photo photo = photoService.uploadPhoto(file);
        PhotoDto photoDto = photoMapper.toDto(photo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(photoDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String id) {
        return photoService.getPhotoAsResource(id).map(photo ->
                ResponseEntity.ok()
                        .contentType(FileUtil.getMediaType(photo))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                        .body(photo)
        ).orElse(ResponseEntity.notFound().build());
    }
}
