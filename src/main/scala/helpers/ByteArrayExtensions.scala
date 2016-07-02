package helpers

import java.nio.ByteBuffer

object ByteArrayExtensions {
  implicit class ExtendedByteArray(arrayOfBytes: Array[Byte]) extends scala.AnyRef {
    def toArrayOfInts = {
      arrayOfBytes.grouped(4).map(intBytes => ByteBuffer.wrap(intBytes).getInt).toArray
    }
  }
}