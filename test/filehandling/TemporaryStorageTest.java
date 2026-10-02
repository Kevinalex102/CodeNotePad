package filehandling;

import java.nio.file.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author kevin
 */
public class TemporaryStorageTest {

    private Path imagesFolder;
    private Path codeFilesFolder;

    @BeforeEach
    void setUp() {
        TemporaryStorage.clearCache();

        imagesFolder = TemporaryStorage.getImagesFolder();
        codeFilesFolder = TemporaryStorage.getCodeFilesFolder();
    }

    @AfterEach
    void tearDown() {
        TemporaryStorage.clearCache();
    }

    @Test
    void shouldCreateTemporaryDirectories() {
        assertTrue(Files.exists(imagesFolder));
        assertTrue(Files.isDirectory(imagesFolder));

        assertTrue(Files.exists(codeFilesFolder));
        assertTrue(Files.isDirectory(codeFilesFolder));
    }

    @Test
    void shouldReturnSameImagesFolder() {
        Path first = TemporaryStorage.getImagesFolder();
        Path second = TemporaryStorage.getImagesFolder();

        assertEquals(first, second);
    }

    @Test
    void shouldReturnSameCodeFilesFolder() {
        Path first = TemporaryStorage.getCodeFilesFolder();
        Path second = TemporaryStorage.getCodeFilesFolder();

        assertEquals(first, second);
    }

    @Test
    void shouldClearFilesFromTemporaryDirectories() throws Exception {
        Path imageFile = imagesFolder.resolve("test-image.png");
        Path codeFile = codeFilesFolder.resolve("test-code.java");

        Files.writeString(imageFile, "image test");
        Files.writeString(codeFile, "code test");

        assertTrue(Files.exists(imageFile));
        assertTrue(Files.exists(codeFile));

        TemporaryStorage.clearCache();

        assertFalse(Files.exists(imageFile));
        assertFalse(Files.exists(codeFile));
    }

    @Test
    void shouldKeepTemporaryDirectoriesAfterClearingCache() throws Exception {
        Files.writeString(
                imagesFolder.resolve("test-image.png"),
                "image test"
        );

        Files.writeString(
                codeFilesFolder.resolve("test-code.java"),
                "code test"
        );

        TemporaryStorage.clearCache();

        assertTrue(Files.exists(imagesFolder));
        assertTrue(Files.isDirectory(imagesFolder));

        assertTrue(Files.exists(codeFilesFolder));
        assertTrue(Files.isDirectory(codeFilesFolder));
    }

    @Test
    void shouldClearCacheWhenCalledBeforeGettingFolders() {
        TemporaryStorage.clearCache();

        assertTrue(Files.exists(TemporaryStorage.getImagesFolder()));
        assertTrue(Files.exists(TemporaryStorage.getCodeFilesFolder()));
    }
}