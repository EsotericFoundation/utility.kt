package foundation.esoteric.utility.resource

import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.jar.JarFile

/**
 * A collection of static methods that help when working with **resource** files.
 */
class ResourceUtility {
    companion object {
        /**
         * This method loops through a folder in the **resources** folder and returns the paths of all files stored in said folder.
         * @param path The path to the folder in **resources** to get the file paths of.
         * @return A set of all the paths of all files stored in the folder specified by the path parameter.
         */
        fun getResourceFilePaths(path: Path): Set<Path> {
            val filePaths = mutableSetOf<Path>()

            val url = object {}.javaClass.classLoader.getResource(path.toString())?.toURI()

            requireNotNull(url) { "The specified resource URL could not be found." }

            when (url.scheme) {
                "file" -> {
                    val folderFile = File(url)
                    folderFile.walkTopDown().forEach { file ->
                        if (file.isFile) {
                            filePaths.add(Paths.get("$path/${file.relativeTo(folderFile).path}"))
                        }
                    }
                }
                "jar" -> {
                    try {
                        val jarFileUrl = url.toURL().openConnection() as java.net.JarURLConnection

                        JarFile(jarFileUrl.jarFileURL.path).use { jarFile ->
                            val entries = jarFile.entries()

                            while (entries.hasMoreElements()) {
                                val entry = entries.nextElement()

                                if (entry.name.startsWith(path.toString()) && !entry.isDirectory) {
                                    filePaths.add(Paths.get(entry.name))
                                }
                            }
                        }
                    } catch (exception: Exception) {
                        throw IllegalStateException("Failed to access JAR contents.", exception)
                    }
                }
            }

            return filePaths
        }

        /**
         * This method loops through a folder in the **resources** folder and returns the paths of all files stored in said folder.
         * @param path The string path to the folder in **resources** to get the file paths of.
         * @return A set of all the paths of all files stored in the folder specified by the path parameter.
         */
        fun getResourceFilePaths(path: String): Set<Path> {
            return getResourceFilePaths(Path.of(path))
        }

        /**
         * This method saves a resource in the "resources" folder to the file specified as the `outputPath`.
         * @param resourcePath The path to the resource file.
         * @param outputPath The path to the output file.
         */
        fun saveResource(resourcePath: Path, outputPath: Path) {
            val resourceStream: InputStream? = object {}.javaClass.classLoader.getResourceAsStream(resourcePath.toString())
            requireNotNull(resourceStream) { "Resource '$resourcePath' could not be found." }

            Files.createDirectories(outputPath.parent)

            Files.copy(resourceStream, outputPath, StandardCopyOption.REPLACE_EXISTING)
            resourceStream.close()
        }

        /**
         * This method saves a resource in the "resources" folder to the file specified as the `outputPath`.
         * @param resourcePath The path to the resource file.
         * @param outputPath The path to the output file.
         */
        fun saveResource(resourcePath: String, outputPath: String) {
            saveResource(Path.of(resourcePath), Path.of(outputPath))
        }
    }
}
