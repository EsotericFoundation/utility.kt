package foundation.esoteric.utility.file

import foundation.esoteric.utility.byte.ByteUtility
import net.lingala.zip4j.ZipFile
import java.io.File
import java.io.IOException
import java.nio.file.Path

/**
 * This class provides various static utility methods for working with files.
 */
class FileUtility {
    companion object {
        /**
         * This method checks if a directory is **recursively empty**, which means either:
         * - The directory is empty.
         * - The directory contains only **recursively empty** directories.
         * @param directory The directory to check for recursive emptiness.
         */
        fun isRecursivelyEmpty(directory: File): Boolean {
            require(directory.exists()) { "The specified directory does not exist." }
            require(directory.isDirectory) { "The specified path is not a directory." }

            val files = directory.listFiles()

            if (files == null || files.isEmpty()) {
                return true
            }

            for (file in files) {
                if (file.isFile) {
                    return false
                } else if (file.isDirectory) {
                    if (!isRecursivelyEmpty(file)) {
                        return false
                    }
                }
            }

            return true
        }

        /**
         * This method checks if a directory is **recursively empty**, which means either:
         * - The directory is empty.
         * - The directory contains only **recursively empty** directories.
         * @param directoryPath The path of the directory to check for recursive emptiness.
         */
        fun isRecursivelyEmpty(directoryPath: Path): Boolean {
            return isRecursivelyEmpty(directoryPath.toFile())
        }

        /**
         * This method checks if a directory is **recursively empty**, which means either:
         * - The directory is empty.
         * - The directory contains only **recursively empty** directories.
         * @param directoryPath The path of the directory to check for recursive emptiness.
         */
        fun isRecursivelyEmpty(directoryPath: String): Boolean {
            return isRecursivelyEmpty(File(directoryPath))
        }

        /**
         * This method returns the SHA-1 hash of a file.
         * @param file The file to get the SHA-1 hash of.
         */
        fun getSha1Hash(file: File): String {
            require(file.exists()) { "File does not exist." }
            require(file.isFile) { "File is not a file." }

            val bytes = file.readBytes()
            return ByteUtility.getSha1Hash(bytes)
        }

        /**
         * This method returns the SHA-1 hash of a file.
         * @param path The path of the file to get the SHA-1 hash of.
         */
        fun getSha1Hash(path: Path): String {
            return getSha1Hash(path.toFile())
        }

        /**
         * This method returns the SHA-1 hash of a file.
         * @param filePath The string path of the file to get the SHA-1 hash of.
         */
        fun getSha1Hash(filePath: String): String {
            return getSha1Hash(Path.of(filePath))
        }

        /**
         * This method creates a zip archive of the file specified and outputs it at the specified location.
         * @param sourceFolder The folder to create a zip archive of.
         * @param zipFile The File location of where to output the zip archive.
         */
        @Throws(IOException::class)
        fun zipFolder(sourceFolder: File, zipFile: File) {
            requireNotNull(sourceFolder.listFiles()) { "Cannot list files of the source folder." }
            require(sourceFolder.exists()) { "Source folder does not exist." }

            ZipFile(zipFile).use { zipFileInstance ->
                for (file in sourceFolder.listFiles()!!) {
                    if (file.isDirectory) {
                        zipFileInstance.addFolder(file)
                    } else {
                        zipFileInstance.addFile(file)
                    }
                }
            }
        }

        /**
         * This method creates a zip archive of the file specified and outputs it at the specified location.
         * @param sourceFolderPath The Path of the folder to create a zip archive of.
         * @param zipFilePath The Path to where to output the zip archive.
         */
        @Throws(IOException::class)
        fun zipFolder(sourceFolderPath: Path, zipFilePath: Path) {
            zipFolder(sourceFolderPath.toFile(), zipFilePath.toFile())
        }

        /**
         * This method creates a zip archive of the file specified and outputs it at the specified location.
         * @param sourceFolderPath The path of the folder to create a zip archive of.
         * @param zipFilePath The Path to where to output the zip archive.
         */
        @Throws(IOException::class)
        fun zipFolder(sourceFolderPath: String, zipFilePath: String) {
            zipFolder(Path.of(sourceFolderPath), Path.of(zipFilePath))
        }
    }
}
