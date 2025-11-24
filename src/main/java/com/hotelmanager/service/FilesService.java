package com.hotelmanager.service;

import com.hotelmanager.config.FeignConfiguration;
import com.hotelmanager.model.dto.feign.RoomPhotoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "${files.service.name}", url = "${files.service.url}",
        path = "${files.service.path}", configuration = FeignConfiguration.class)
public interface FilesService {

    @PostMapping("/create-file")
    void createFile();

    @PostMapping(value = "/upload-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RoomPhotoDto uploadPhotos(
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("roomId") String roomId,
            @RequestPart("roomTypeId") String roomTypeId);

    @GetMapping("/room/{roomId}")
    List<RoomPhotoDto> getPhotosByRoom(@PathVariable String roomId);

    @GetMapping("/room-type/{roomTypeId}")
    List<RoomPhotoDto> getPhotosByRoomType(@PathVariable String roomTypeId);

    @DeleteMapping("/{publicId}")
    void deletePhoto(@PathVariable String publicId);

    @DeleteMapping("/room/{roomId}")
    void deletePhotoByRoom(@PathVariable String roomId);

    @DeleteMapping("/room-type/{roomTypeId}")
    void deletePhotoByRoomType(@PathVariable String roomTypeId);
}
