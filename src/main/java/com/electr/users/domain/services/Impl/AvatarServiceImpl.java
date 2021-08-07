package com.electr.users.domain.services.Impl;

import com.electr.users.core.FileStorageConfiguration;
import com.electr.users.domain.services.AvatarService;
import com.electr.users.exceptions.AllException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Slf4j
@Service
public class AvatarServiceImpl implements AvatarService {

    private Path fileStorageLocation;

    public AvatarServiceImpl(FileStorageConfiguration fileStorageConfiguration) {
        String project = System.getProperty("user.dir") + "./pictures/usuarios";
        fileStorageConfiguration.setUploadDir(project);
        this.fileStorageLocation = Paths.get(fileStorageConfiguration.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);

        } catch (Exception ex) {
            throw new AllException("Este local não foi encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public String savePictureOnDisc(MultipartFile picture) {

        DateTimeFormatter dataFormatted = DateTimeFormatter.ofPattern("HHmmss");
        String hourFormatted = dataFormatted.format(LocalDateTime.now());

        String pictureName = StringUtils.cleanPath(hourFormatted + "-" + picture.getOriginalFilename());

        String pictureSpaceRemove = pictureName.replaceAll("\\s+","");

        try {
            Path targetLocation = this.fileStorageLocation.resolve(pictureSpaceRemove).normalize();
            Files.copy(picture.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return pictureSpaceRemove;

        } catch (IOException ex) {
            throw new AllException("This image not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public String createImageInServer(MultipartFile picture){
        String pictureName = savePictureOnDisc(picture);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/usuarios/search-picture/").path(pictureName).toUriString();
    }

    @Override
    public Resource loadPicture(String pictureName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(pictureName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new AllException("File not found " + pictureName);
            }
        } catch (MalformedURLException ex) {
            throw new AllException("File not found " + pictureName);
        }
    }
}
