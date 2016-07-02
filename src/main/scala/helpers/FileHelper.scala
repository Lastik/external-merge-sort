package helpers

import java.io._
import java.nio.file.{Files, Paths}

import scala.util.Random

import ByteArrayExtensions._

object FileHelper {

  def generateBinaryFileWithRandomIntNumbers(fileName: String, fileSize: Int) {
    val fos = new FileOutputStream(fileName)
    val dos = new DataOutputStream(fos)

    val sizeOfIntInBytes = 4
    val intNumbersPerFile = fileSize / sizeOfIntInBytes

    (0 until intNumbersPerFile).foreach(_ => {
      val randomInt = Random.nextInt()
      dos.writeInt(randomInt)
    })

    dos.flush()
    dos.close()
  }

  def prettyPrintBinaryFileWithIntsToTextFile(fileName: String, outputFileName: String) {
    val arrayWithInts = Files.readAllBytes(Paths.get(fileName)).toArrayOfInts
    val outFile = new PrintWriter(outputFileName)
    outFile.print(arrayWithInts.mkString(", "))
    outFile.close()
  }

  def getFileSize(fileName: String) = {
    new File(fileName).length().toInt
  }

  def createDirectoryIfNotExists(directoryPath: String) = {
    val dir = new File(directoryPath)
    if (!dir.exists()) {
      dir.mkdir()
    }
  }

  def delete(file: File) {
    if (file.isDirectory) {
      for (innerFile <- file.listFiles()) {
        delete(innerFile)
      }
    }
    if (!file.delete())
      throw new FileNotFoundException("Failed to delete file: " + file)
  }

  def delete(file: String) {
    delete(new File(file))
  }
}
