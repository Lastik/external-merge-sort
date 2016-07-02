package sort.merge

import java.io.FileInputStream

import helpers.ByteArrayExtensions._

class BinaryIntFileChunkedReader(fileName: String, chunkSize: Int) {

  val inputFileStream = new FileInputStream(fileName)

  val chunkBuffer = new Array[Byte](chunkSize)

  private var internalChunksStream = Stream
    .continually((inputFileStream.read(chunkBuffer), chunkBuffer))
    .takeWhile {
      case (bytesRead, _) => bytesRead != -1
    }.zipWithIndex.map{case ((bytesRead, chunkBytes), chunkIndex) =>
    (chunkIndex, chunkBytes.toArrayOfInts)}

  private def getCurrentChunk: Option[Array[Int]] =
    internalChunksStream.map { case (chunkIndex, chunkInts) => chunkInts}.headOption

  private def shiftToNextChunk() = internalChunksStream = internalChunksStream.tail

  var indexOfElementWithinChunk: Int = 0

  def currentElement: Option[Int] = {
    getCurrentChunk match {
      case Some(currentChunk) =>
        if (indexOfElementWithinChunk < currentChunk.length) {
          Some(currentChunk(indexOfElementWithinChunk))
        }
        else None
      case None => None
    }
  }

  def shiftToNextElement: Boolean = {
    getCurrentChunk match {
      case Some(currentChunk) =>
        if (indexOfElementWithinChunk + 1 < currentChunk.length) {
          indexOfElementWithinChunk = indexOfElementWithinChunk + 1
          true
        }
        else {
          shiftToNextChunk()
          indexOfElementWithinChunk = 0
          currentElement match {
            case Some(elt) => true
            case None => false
          }
        }
      case None => false
    }
  }

  def chunksStream = internalChunksStream

  def close() = inputFileStream.close()
}
