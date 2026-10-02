package filehandling;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author kevin
 */
public class CodeFileTest {

    @BeforeEach
    void setUp() {
        TemporaryStorage.clearCache();
    }

    @AfterEach
    void tearDown() {
        TemporaryStorage.clearCache();
    }

    @Test
    void shouldCreateCodeFile() {
        CodeFile codeFile = new CodeFile(
                "System.out.println(\"Hello\");",
                1,
                ".java"
        );

        assertNotNull(codeFile.getFile());
        assertTrue(codeFile.getFile().exists());
        assertTrue(codeFile.getFile().isFile());
    }

    @Test
    void shouldWriteInitialTextToFile() throws Exception {
        String text = "System.out.println(\"Hello\");";

        CodeFile codeFile = new CodeFile(
                text,
                1,
                ".java"
        );

        String content = Files.readString(codeFile.getFile().toPath());

        assertEquals(text, content);
    }

    @Test
    void shouldCreateFileWithCorrectName() {
        CodeFile codeFile = new CodeFile(
                "Hello",
                42,
                ".java"
        );

        assertEquals(
                "code42.java",
                codeFile.getFile().getName()
        );
    }

    @Test
    void shouldCreateFileInsideTemporaryCodeFilesFolder() {
        CodeFile codeFile = new CodeFile(
                "Hello",
                1,
                ".java"
        );

        Path expectedFolder = TemporaryStorage.getCodeFilesFolder();

        assertEquals(
                expectedFolder.toAbsolutePath().normalize(),
                codeFile.getFile()
                        .toPath()
                        .getParent()
                        .toAbsolutePath()
                        .normalize()
        );
    }

    @Test
    void shouldReturnCreatedFile() {
        CodeFile codeFile = new CodeFile(
                "Hello",
                1,
                ".java"
        );

        assertNotNull(codeFile.getFile());
        assertTrue(codeFile.getFile().exists());
    }

    @Test
    void shouldReplaceFileContents() throws Exception {
        CodeFile codeFile = new CodeFile(
                "Original text",
                1,
                ".java"
        );

        String newText = "New text";

        codeFile.write(newText);

        assertEquals(
                newText,
                Files.readString(codeFile.getFile().toPath())
        );
    }

    @Test
    void shouldPreserveFileAfterWritingNewText() throws Exception {
        CodeFile codeFile = new CodeFile(
                "Original text",
                1,
                ".java"
        );

        Path file = codeFile.getFile().toPath();

        codeFile.write("New text");

        assertTrue(Files.exists(file));
        assertEquals("New text", Files.readString(file));
    }

    @Test
    void shouldCreateDifferentFilesForDifferentIds() {
        CodeFile first = new CodeFile(
                "First",
                1,
                ".java"
        );

        CodeFile second = new CodeFile(
                "Second",
                2,
                ".java"
        );

        assertNotEquals(
                first.getFile().getAbsolutePath(),
                second.getFile().getAbsolutePath()
        );

        assertTrue(first.getFile().exists());
        assertTrue(second.getFile().exists());
    }

    @Test
    void shouldSupportDifferentFileTypes() {
        CodeFile javaFile = new CodeFile(
                "class Test {}",
                1,
                ".java"
        );

        CodeFile txtFile = new CodeFile(
                "Hello",
                2,
                ".txt"
        );

        assertEquals("code1.java", javaFile.getFile().getName());
        assertEquals("code2.txt", txtFile.getFile().getName());
    }
}