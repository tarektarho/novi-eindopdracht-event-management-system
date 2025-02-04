package nl.novi.event_management_system.services;

import nl.novi.event_management_system.exceptions.FileDownloadException;
import nl.novi.event_management_system.models.UserPhoto;
import nl.novi.event_management_system.repositories.UserPhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPhotoServiceTest {

    @Mock
    private UserPhotoRepository userPhotoRepository;

    @InjectMocks
    private UserPhotoService userPhotoService;
    private final Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "test-uploads");

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(tempDir);
        //userPhotoService = new UserPhotoService(tempDir.toString(), userPhotoRepository);
    }

    @Test
    void storeFile_SuccessfullyStoresFile() throws IOException {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        String fileName = "test.jpg";
        when(mockFile.getOriginalFilename()).thenReturn(fileName);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream("dummy content".getBytes()));

        UserPhoto userPhoto = new UserPhoto(fileName);
        when(userPhotoRepository.save(any(UserPhoto.class))).thenAnswer(invocation -> {
            UserPhoto userPhotoArg = invocation.getArgument(0);
            userPhotoArg.setFileName(fileName);
            return userPhotoArg;
        });
        //when(userPhotoRepository.save(any(UserPhoto.class))).thenReturn(userPhoto);

        // Act
        String storedFileName = userPhotoService.storeFile(mockFile);

        // Assert
        assertEquals(fileName, storedFileName);
        assertTrue(Files.exists(tempDir.resolve(fileName)));
        verify(userPhotoRepository).save(any(UserPhoto.class));
    }

    @Test
    void storeFile_ThrowsException_WhenFileHasNoName() {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> userPhotoService.storeFile(mockFile));
        verifyNoInteractions(userPhotoRepository);
    }

    @Test
    void downLoadFile_ReturnsResource_WhenFileExists() throws IOException {
        // Arrange
        String fileName = "test.jpg";
        Path filePath = tempDir.resolve(fileName);
        Files.write(filePath, "dummy content".getBytes());

        // Act
        Resource resource = userPhotoService.downLoadFile(fileName);

        // Assert
        assertNotNull(resource);
        assertTrue(resource.exists());
    }

    @Test
    void downLoadFile_ThrowsException_WhenFileDoesNotExist() {
        // Arrange
        String fileName = "non_existent.jpg";

        // Act & Assert
        FileDownloadException exception = assertThrows(FileDownloadException.class, () -> userPhotoService.downLoadFile(fileName));
        assertEquals("the file doesn't exist or not readable", exception.getMessage());
    }

    @Test
    void downLoadFile_ThrowsException_WhenFileIsNotReadable() throws IOException {
        // Arrange
        String fileName = "unreadable.jpg";
        Path filePath = tempDir.resolve(fileName);
        Files.createFile(filePath);
        Files.setPosixFilePermissions(filePath, PosixFilePermissions.fromString("---------")); // Make file unreadable

        // Act & Assert
        FileDownloadException exception = assertThrows(FileDownloadException.class, () -> userPhotoService.downLoadFile(fileName));
        assertEquals("the file doesn't exist or not readable", exception.getMessage());
    }
}
