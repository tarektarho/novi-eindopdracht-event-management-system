package nl.novi.event_management_system.services;

import nl.novi.event_management_system.exceptions.FileDownloadException;
import nl.novi.event_management_system.models.UserPhoto;
import nl.novi.event_management_system.repositories.UserPhotoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * A service class that handles the storage and retrieval of user photos.
 */
@Service
public class UserPhotoService {
    private final Path fileStoragePath;
    private final String fileStorageLocation;
    private final UserPhotoRepository userPhotoRepository;

    /**
     * Constructor for the UserPhotoService class.
     *
     * @param fileStorageLocation The location where the files are stored.
     * @param userPhotoRepository The repository for user photos.
     * @throws IOException If an I/O error occurs.
     */
    public UserPhotoService(@Value("${my.upload_location}") String fileStorageLocation, UserPhotoRepository userPhotoRepository) throws IOException {
        fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        this.fileStorageLocation = fileStorageLocation;
        this.userPhotoRepository = userPhotoRepository;
        Files.createDirectories(fileStoragePath);
    }

    /**
     * Stores a file in the file storage location.
     *
     * @param file The file to store.
     * @return The name of the file.
     * @throws IOException If an I/O error occurs.
     */
    public String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path filePath = Paths.get(fileStoragePath + File.separator + fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        userPhotoRepository.save(new UserPhoto(fileName));
        return fileName;
    }

    /**
     * Retrieves a file from the file storage location.
     *
     * @param fileName The name of the file to retrieve.
     * @return The file as a Resource.
     */
    public Resource downLoadFile(String fileName) {
        Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Resource resource;

        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Issue in reading the file", e); //Todo cover this line with test.
        }

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileDownloadException("the file doesn't exist or not readable");
        }
    }
}
