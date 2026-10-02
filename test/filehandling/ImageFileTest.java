package filehandling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author kevin
 */
public class ImageFileTest {

    private Path tempFolder;

    @BeforeEach
    void setUp() throws Exception {
        tempFolder = Files.createTempDirectory("codenotepad-image-test");
    }

    @AfterEach
    void tearDown() throws Exception {
        TemporaryStorage.clearCache();

        if (tempFolder != null) {
            Files.walk(tempFolder)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (Exception ignored) {
                        }
                    });
        }
    }

    private Path createPng(String fileName) throws Exception {
        Path imagePath = tempFolder.resolve(fileName);

        BufferedImage image = new BufferedImage(
                100,
                80,
                BufferedImage.TYPE_INT_RGB
        );

        ImageIO.write(
                image,
                "png",
                imagePath.toFile()
        );

        return imagePath;
    }

    @Test
    void shouldCopyImageToTemporaryStorage() throws Exception {
        Path originalImage = createPng("original.png");

        File temporaryFile = ImageFile.copyToTemporary(
                originalImage.toFile(),
                7
        );

        assertNotNull(temporaryFile);
        assertTrue(temporaryFile.exists());

        assertEquals(
                "image7.png",
                temporaryFile.getName()
        );

        assertTrue(ImageFile.isImage(temporaryFile));
    }

    @Test
    void shouldPreserveImageExtensionWhenCopyingToTemporaryStorage() throws Exception {

        Path originalImage = createPng("photo.png");

        File temporaryFile = ImageFile.copyToTemporary(
                originalImage.toFile(),
                42
        );

        assertEquals(
                "image42.png",
                temporaryFile.getName()
        );
    }

    @Test
    void shouldReplaceExistingTemporaryImage() throws Exception {
        Path originalImage = createPng("original.png");

        Path temporaryPath = TemporaryStorage
                .getImagesFolder()
                .resolve("image7.png");

        Files.writeString(
                temporaryPath,
                "old content"
        );

        ImageFile.copyToTemporary(
                originalImage.toFile(),
                7
        );

        assertTrue(Files.exists(temporaryPath));
        assertTrue(ImageFile.isImage(temporaryPath.toFile()));
    }

    @Test
    void shouldCopyImageToProjectFolder() throws Exception {
        Path originalImage = createPng("original.png");

        ImageFile imageFile = new ImageFile(
                originalImage.toFile(),
                1
        );

        Path projectImages = tempFolder.resolve("images");
        Files.createDirectories(projectImages);

        File copiedFile = imageFile.copyTo(
                projectImages.toFile()
        );

        assertNotNull(copiedFile);
        assertTrue(copiedFile.exists());

        assertEquals(
                "image1.png",
                copiedFile.getName()
        );

        assertTrue(ImageFile.isImage(copiedFile));
    }

    @Test
    void shouldUpdateInternalFileAfterCopyingToProjectFolder() throws Exception {

        Path originalImage = createPng("original.png");

        ImageFile imageFile = new ImageFile(
                originalImage.toFile(),
                1
        );

        Path projectImages = tempFolder.resolve("images");
        Files.createDirectories(projectImages);

        File copiedFile = imageFile.copyTo(
                projectImages.toFile()
        );

        assertEquals(
                copiedFile,
                imageFile.getFile()
        );
    }

    @Test
    void shouldRecognizeValidImage() throws Exception {
        Path image = createPng("valid.png");

        assertTrue(
                ImageFile.isImage(image.toFile())
        );
    }

    @Test
    void shouldRejectInvalidImage() throws Exception {
        Path invalidFile = tempFolder.resolve("invalid.png");

        Files.writeString(
                invalidFile,
                "This is not an image"
        );

        assertFalse(
                ImageFile.isImage(invalidFile.toFile())
        );
    }

    @Test
    void shouldRejectNullAsImage() {
        assertFalse(
                ImageFile.isImage(null)
        );
    }

    @Test
    void shouldRejectDirectoryAsImage() throws Exception {
        Path directory = Files.createDirectory(
                tempFolder.resolve("directory")
        );

        assertFalse(
                ImageFile.isImage(directory.toFile())
        );
    }

    @Test
    void shouldRejectImageWithoutExtension() throws Exception {
        Path imageWithoutExtension =
                createPng("image");

        assertThrows(
                IllegalArgumentException.class,
                () -> ImageFile.copyToTemporary(
                        imageWithoutExtension.toFile(),
                        1
                )
        );
    }
}
