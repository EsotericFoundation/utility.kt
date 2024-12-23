package foundation.esoteric.utility.resource

import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.io.path.Path
import kotlin.test.*

class ResourceUtilityTest {

    private val run = File("run")

    @BeforeTest fun createRunDirectory() {
        run.mkdir()
    }

    @Test fun resourcesListIsCorrect() {
        val resourcePaths = Path("resource").resourceFilePaths()

        assertEquals(setOf(Path("resource/ResourceUtilityTest/Test Folder/Test File.txt"), Path("resource/ResourceUtilityTest/Test File.txt")), resourcePaths)
    }

    @Test fun getResourcePathsThrowsCorrectly() {
        assertThrows<IllegalArgumentException> {
            Path("some random path that does not exist").resourceFilePaths()
        }
    }

    @Test fun savingResourceWorks() {
        val saveLocation = File(run, "Test File.txt")
        val resourcePath = Path("resource/ResourceUtilityTest/Test File.txt")
        resourcePath.saveResource(saveLocation)

        assertTrue(saveLocation.exists())
        assertTrue(saveLocation.isFile)
        assertFalse(saveLocation.isDirectory)
        assertEquals("This file is used to test the resource utility.", saveLocation.readText().trimEnd('\n', '\r'))
    }

    @Test fun savingResourcesWorks() {
        val saveFolder = File(run, "Save Folder")
        Path("resource").saveResources(saveFolder)

        assertTrue(saveFolder.exists())
        assertTrue(saveFolder.isDirectory)
        assertFalse(saveFolder.isFile)

        val resourceUtilityTest = File(saveFolder, "ResourceUtilityTest")
        assertTrue(resourceUtilityTest.exists())
        assertTrue(resourceUtilityTest.isDirectory)
        assertFalse(resourceUtilityTest.isFile)

        val deepFile = File(run, "Save Folder/ResourceUtilityTest/Test Folder/Test File.txt")
        assertTrue(deepFile.exists())
        assertTrue(deepFile.isFile)
        assertFalse(deepFile.isDirectory)
        assertEquals("This file is used to test the resource utility.", deepFile.readText().trimEnd('\n', '\r'))
    }

    @AfterTest fun deleteRunDirectory() {
        FileUtils.deleteDirectory(run)
    }
}
