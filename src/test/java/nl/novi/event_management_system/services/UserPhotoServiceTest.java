package nl.novi.event_management_system.services;

import nl.novi.event_management_system.exceptions.FileDownloadException;
import nl.novi.event_management_system.models.UserPhoto;
import nl.novi.event_management_system.repositories.UserPhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserPhotoServiceTest {

    @Mock
    private UserPhotoRepository userPhotoRepository;

    @Mock
    private MultipartFile multipartFile;

    private UserPhotoService userPhotoService;

    private final String fileStorageLocation = "test-uploads";

    @BeforeEach
    public void setUp() throws IOException {
        userPhotoService = new UserPhotoService(fileStorageLocation, userPhotoRepository);

        Files.createDirectories(Paths.get(fileStorageLocation));
    }

    @Test
    public void testStoreFile() throws IOException {
        // Arrange
        String fileName = "test-file.jpg";
        when(multipartFile.getOriginalFilename()).thenReturn(fileName);
        when(multipartFile.getInputStream()).thenReturn(Files.newInputStream(Paths.get("src/test/resources/test-file.jpg")));

        // Act
        String storedFileName = userPhotoService.storeFile(multipartFile);

        // Assert
        assertEquals(fileName, storedFileName);
        verify(userPhotoRepository, times(1)).save(any(UserPhoto.class));
    }

    @Test
    public void testDownloadFile() throws IOException {
        // Arrange
        String fileName = "test-file-2.jpg";
        Path filePath = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Files.copy(Paths.get("src/test/resources/test-file.jpg"), filePath);

        // Act
        Resource resource = userPhotoService.downLoadFile(fileName);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());

        // Clean up
        Files.deleteIfExists(filePath);
    }

    @Test
    public void testDownloadFile_FileDoesNotExist() {
        // Arrange
        String fileName = "non-existent-file.jpg";

        // Act & Assert
        assertThrows(FileDownloadException.class, () -> userPhotoService.downLoadFile(fileName));
    }

    @Test
    public void testDownloadFile_FileNotReadable() throws IOException {
        // Arrange
        String fileName = "unreadable-file.jpg";
        Path filePath = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);
        Files.createFile(filePath);
        filePath.toFile().setReadable(false);

        // Act & Assert
        assertThrows(FileDownloadException.class, () -> userPhotoService.downLoadFile(fileName));

        // Clean up
        Files.deleteIfExists(filePath);
    }
}