import helpers.FileHelper
import sort.merge.MergeSort

object Main extends App {

  val fileSize = 100000
  val inputBinaryFileName = "numbers_unordered.dat"
  val inputTextPrettifiedFileName = "numbers_unordered.txt"
  val outputBinaryFileName = "numbers_ordered.dat"
  val outputTextPrettifiedFileName = "numbers_ordered.txt"

  FileHelper.generateBinaryFileWithRandomIntNumbers(inputBinaryFileName, fileSize)
  MergeSort.sortBinaryFileWithInts(inputBinaryFileName, outputBinaryFileName, chunksCount = 10)

  FileHelper.prettyPrintBinaryFileWithIntsToTextFile(inputBinaryFileName, inputTextPrettifiedFileName)
  FileHelper.prettyPrintBinaryFileWithIntsToTextFile(outputBinaryFileName, outputTextPrettifiedFileName)
}