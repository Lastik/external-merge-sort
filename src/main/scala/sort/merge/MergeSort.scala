package sort.merge

import java.io.{DataOutputStream, FileOutputStream}

import helpers.FileHelper

object MergeSort {

  import FileHelper._

  def sortBinaryFileWithInts(fileName: String, outputFileName: String, chunksCount: Int) {

    val fileSize = getFileSize(fileName)

    val chunkSize = fileSize / chunksCount

    val fileChunkedReader = new BinaryIntFileChunkedReader(fileName, chunkSize)

    val tmpDirectoryPath = ".tmp"

    def getChunkFilePathByIndex(chunkIndex: Int) = {
      s"$tmpDirectoryPath/chunk_$chunkIndex.dat"
    }

    createDirectoryIfNotExists(tmpDirectoryPath)

    fileChunkedReader.chunksStream.foreach {
      case (chunkIndex, chunkInts) =>
        val tmpFileForChunkPath = getChunkFilePathByIndex(chunkIndex)
        writeIntsArrayToFile(getChunkFilePathByIndex(chunkIndex), chunkInts.sorted)
    }

    val mergePortionPerChunk = chunkSize / chunksCount

    val chunksReaders = (0 until chunksCount).map(chunkIndex => {
      val tmpFileForChunkPath = getChunkFilePathByIndex(chunkIndex)
      new BinaryIntFileChunkedReader(tmpFileForChunkPath, mergePortionPerChunk)
    }).toList

    val fos = new FileOutputStream(outputFileName)
    val dos = new DataOutputStream(fos)

    while (!chunksReaders.forall(_.currentElement.isEmpty)) {
      val nonEmptyReaders = chunksReaders.filterNot(_.currentElement.isEmpty)

      val (chunkReaderWithMinElt, minElt) = nonEmptyReaders.map(
        reader => (reader, reader.currentElement.get)).
        minBy { case (reader, elt) => elt }

      chunkReaderWithMinElt.shiftToNextElement

      dos.writeInt(minElt)
    }
    dos.close()

    delete(tmpDirectoryPath)
  }

  private def writeIntsArrayToFile(filePath: String, intsArray: Array[Int]) {
    val fos = new FileOutputStream(filePath)
    val dos = new DataOutputStream(fos)

    intsArray.foreach(intVal => {
      dos.writeInt(intVal)
    })

    dos.flush()
    dos.close()
  }

}
