package kr.co.releasetech.kiosk.manager

import android.os.Handler
import android.os.Looper
import kr.co.releasetech.kiosk.utils.DebugUtils
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.concurrent.thread

class ZipManager {
    companion object{
        private const val TAG = "ZipManager"
        private const val BUFFER = 80000
        private const val BUFFER_SIZE = 1024 * 2
        private const val COMPRESSION_LEVEL = 8
    }


    fun zip(files: Array<String>, zipFileName: String){
        try {
            var origin: BufferedInputStream? = null
            val dest = FileOutputStream(zipFileName)
            val out = ZipOutputStream(BufferedOutputStream(dest))

            var data = ByteArray(BUFFER)

            for (file in files){
                val fileInputStream = FileInputStream(file)
                origin = BufferedInputStream(fileInputStream, BUFFER)

                val entry = ZipEntry(file.substring(file.lastIndexOf("/" + 1)))
                out.putNextEntry(entry)
                var count = 0
                while ((count != origin.read(data, 0, BUFFER)) != null){
                    out.write(data, 0, count)

                }
                origin.close()
            }
            out.close()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    fun unzip(zipFile: String, targetLocation : String){
        val thread = Thread()
        thread.run {
            dirChecker(targetLocation)

            try{
                val fileInputStream = FileInputStream(zipFile)
                val zipInputStream = ZipInputStream(fileInputStream)
                var entry: ZipEntry? = zipInputStream.nextEntry



                while (entry != null){
                    DebugUtils.setLog(TAG, "entry.name : ${entry.name}")
                    entry.let { entry->
                        if(entry.isDirectory) dirChecker(entry.name)
                        else{
                            val fileOutputStream = FileOutputStream(targetLocation + "/" + entry.name)
                            val bufferedInputStream = BufferedInputStream(zipInputStream)
                            val bufferedOutputStream = BufferedOutputStream(fileOutputStream)
                            //var i = zipInputStream.read()

                            val b = ByteArray(1024)
                            var n: Int

                            while ((bufferedInputStream.read(b, 0 , 1024).also { n = it }) >= 0){
                                DebugUtils.setLog(TAG, "n : $n")
                                bufferedOutputStream.write(b, 0 , n)
                            }

                            /*while (i != -1){
                                fileOutputStream.write(i)
                                i = zipInputStream.read()
                            }*/



                            Thread.sleep(100)

                            bufferedOutputStream.close()
                            zipInputStream.closeEntry()
                            fileOutputStream.close()
                        }
                    }
                    entry = zipInputStream.nextEntry
                }
                zipInputStream.close()
                fileInputStream.close()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        thread.start()

        DebugUtils.setLog(TAG, "unzip finish")

    }


    @Throws(java.lang.Exception::class)
    fun zipFolder(inputFolderPath: String?, outZipPath: String?) {
        val sourceFile = File(inputFolderPath)
        if (!sourceFile.isFile && !sourceFile.isDirectory) {
            throw java.lang.Exception("압축 대상의 파일을 찾을 수가 없습니다.")
        }
        var fileOutputStream: FileOutputStream? = null
        var bufferedOutputStream: BufferedOutputStream? = null
        var zipOutputStream: ZipOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(outZipPath)
            bufferedOutputStream = BufferedOutputStream(fileOutputStream)
            zipOutputStream = ZipOutputStream(bufferedOutputStream)
            zipOutputStream.setLevel(COMPRESSION_LEVEL)
            zipEntry(sourceFile, inputFolderPath, zipOutputStream)
            zipOutputStream.finish() // ZipOutputStream finish
        } finally {
            zipOutputStream?.close()
            bufferedOutputStream?.close()
            fileOutputStream?.close()
        }
    }

    @Throws(java.lang.Exception::class)
    fun zipEntry(sourceFile: File, sourcePath: String?, zos: ZipOutputStream) {
        if (sourceFile.isDirectory) {
            if (sourceFile.name.equals(".metadata", true)) {
                return
            }
            val fileArray = sourceFile.listFiles()
            for (i in fileArray.indices) {
                zipEntry(fileArray[i], sourcePath, zos)
            }
        } else {
            var bufferedInputStream: BufferedInputStream? = null
            try {
                val filePath = sourceFile.path
                //String zipEntryName = sFilePath.substring(sourcePath.length() + 1, sFilePath.length());
                val tok = StringTokenizer(filePath, "/")
                var tokLen: Int = tok.countTokens()
                var zipEntryName: String = tok.toString()
                while (tokLen != 0) {
                    tokLen--
                    zipEntryName = tok.nextToken()
                }
                bufferedInputStream = BufferedInputStream(FileInputStream(sourceFile))
                val zipEntry = ZipEntry(zipEntryName)
                zipEntry.time = sourceFile.lastModified()
                zos.putNextEntry(zipEntry)
                val buffer = ByteArray(BUFFER_SIZE)
                var cnt = 0
                while (bufferedInputStream.read(buffer, 0, BUFFER_SIZE).also { cnt = it } != -1) {
                    zos.write(buffer, 0, cnt)
                }
                zos.closeEntry()
            } finally {
                bufferedInputStream?.close()
            }
        }
    }

    private fun dirChecker(dir: String){
        val file = File(dir)
        if(!file.isDirectory) file.mkdirs()

    }


}