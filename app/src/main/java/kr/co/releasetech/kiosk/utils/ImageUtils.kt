package kr.co.releasetech.kiosk.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.ImageDecoder
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kr.co.releasetech.kiosk.R
import java.io.*


object ImageUtils {
    const val TAG = "ImageUtils"
    fun getBitmap(contentResolver: ContentResolver, uri: Uri): Bitmap? {
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getVideoBitmap(context: Context, uri: Uri): Bitmap?{

        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        var bitmap: Bitmap? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(context, uri)
            bitmap = mediaMetadataRetriever.getFrameAtTime(3000, MediaMetadataRetriever.OPTION_CLOSEST)
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            mediaMetadataRetriever?.release()
        }


        return bitmap
    }


    fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {

        val tsLong = System.currentTimeMillis()
        val ts = tsLong.toString()
        val filename = "${ts}.jpg"
        val folder = File(context.getExternalFilesDir(null)?.absolutePath, context.getString(R.string.app_name))
        folder.mkdirs()

        var fOut: OutputStream? = null
        //        File fileOrigin = new File(filepath);
        //
        //        copyFile(fileOrigin, folder.toString()+filename);

        val fileTmp = File(folder.toString(), filename)

        try {
            fOut = FileOutputStream(fileTmp)
        } catch (e: FileNotFoundException) {

            e.printStackTrace()
        }

        bitmap.compress(CompressFormat.JPEG, 100, fOut)

        try {
            fOut!!.flush()
            fOut.close()

        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return fileTmp
    }

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    /* Checks if external storage is available to at least read */
    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

}