package kr.co.releasetech.kiosk.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import kr.co.releasetech.kiosk.AppConst
import kr.co.releasetech.kiosk.R
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import kotlin.concurrent.thread

object FileUtils {
    fun writeExternalStorage(context: Context, uri: Uri?, onSuccess: ((Boolean) -> Unit)) {

        var isSuccess = true

        var outputDir = context.getExternalFilesDir(null)
        var parcelFileDescriptor: ParcelFileDescriptor? = null
        var fileOutputStream: FileOutputStream? = null



        try {
            parcelFileDescriptor = uri?.let { context.contentResolver.openFileDescriptor(it, "w") }
            fileOutputStream = FileOutputStream(parcelFileDescriptor?.fileDescriptor)
        } catch (e: FileNotFoundException) {
            isSuccess = false
            e.printStackTrace()
        }

        var src = File(outputDir, AppConst.ZIP_FILE_NAME)

        var inChannel: FileChannel? = null
        var outChannel: FileChannel? = null

        try {
            inChannel = FileInputStream(src).channel
            outChannel = fileOutputStream?.channel
        } catch (e: FileNotFoundException) {
            isSuccess = false
            e.printStackTrace()
        }

        try {
            inChannel?.transferTo(0, inChannel.size(), outChannel)
        } finally {
            inChannel?.close()
            outChannel?.close()
            fileOutputStream?.close()
            parcelFileDescriptor?.close()

        }


        onSuccess.invoke(isSuccess)

    }

    fun writeInternalStorage(
        context: Context,
        uri: Uri,
        fileName: String?,
        dir: String = "",
        onSuccess: ((Boolean) -> Unit)
    ) {
        val folder = File(context.getExternalFilesDir(null)?.absolutePath, dir)
        folder.mkdirs()

        var outputDir = context.getExternalFilesDir(null)
        var parcelFileDescriptor: ParcelFileDescriptor? = null
        var fileInputStream: FileInputStream? = null

        var isSuccess = true

        try {
            parcelFileDescriptor = uri?.let { context.contentResolver.openFileDescriptor(it, "r") }
            fileInputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        } catch (e: FileNotFoundException) {
            isSuccess = false
            e.printStackTrace()
        }


        var newFile: File? = null
        if (fileName != null) {
            newFile = File(outputDir, dir + fileName)
        }

        var inChannel: FileChannel? = null
        var outChannel: FileChannel? = null

        try {
            inChannel = fileInputStream?.channel
            outChannel = FileOutputStream(newFile).channel
        } catch (e: FileNotFoundException) {
            isSuccess = false
            e.printStackTrace()
        }

        try {
            inChannel?.transferTo(0, inChannel.size(), outChannel)
        } finally {
            inChannel?.close()
            outChannel?.close()
            fileInputStream?.close()
            parcelFileDescriptor?.close()
            onSuccess.invoke(isSuccess)
        }

    }


    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = context.contentResolver?.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {

                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    fun deleteDirectory(file: File) {

        val files = file.listFiles()

        if (file.exists()) {
            files?.map { childFile ->
                if (childFile.isDirectory) {
                    deleteDirectory(childFile)
                } else {

                    childFile.delete()
                }
            }
            file.delete()
        }
    }
}