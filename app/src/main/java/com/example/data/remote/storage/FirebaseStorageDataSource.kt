package com.example.data.remote.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Remote Firebase Storage data source for Kisan Market produce photos.
 * Handles resizing, compression, EXIF rotation, cloud uploads to Firebase Storage,
 * and resilient local file caching for offline viewing.
 */
class FirebaseStorageDataSource(
    private val context: Context,
    private val storage: FirebaseStorage? = try {
        FirebaseStorage.getInstance()
    } catch (e: Throwable) {
        Log.w(TAG, "FirebaseStorage initialization skipped: ${e.message}")
        null
    }
) {
    companion object {
        private const val TAG = "FirebaseStorageDataSource"
    }

    val isStorageAvailable: Boolean
        get() = storage != null

    suspend fun uploadListingImages(
        listingId: String,
        imageUris: List<Uri>
    ): List<String> = withContext(Dispatchers.IO) {
        if (imageUris.isEmpty()) return@withContext emptyList()

        val uploadedUrls = mutableListOf<String>()

        imageUris.forEachIndexed { index, uri ->
            try {
                // If it's already an HTTP/HTTPS remote URL, reuse directly
                if (uri.scheme.equals("http", ignoreCase = true) || uri.scheme.equals("https", ignoreCase = true)) {
                    uploadedUrls.add(uri.toString())
                    return@forEachIndexed
                }

                val bytes = compressAndRotateImageUri(uri)
                if (bytes != null && bytes.isNotEmpty()) {
                    var cloudUrl: String? = null
                    if (storage != null) {
                        try {
                            val storageRef = storage.reference.child("crop_listings/$listingId/photo_${index}_${System.currentTimeMillis()}.jpg")
                            val metadata = StorageMetadata.Builder()
                                .setContentType("image/jpeg")
                                .setCustomMetadata("listingId", listingId)
                                .setCustomMetadata("photoIndex", index.toString())
                                .build()

                            storageRef.putBytes(bytes, metadata).await()
                            cloudUrl = storageRef.downloadUrl.await().toString()
                            Log.d(TAG, "Uploaded photo $index to Firebase Storage: $cloudUrl")
                        } catch (storageEx: Exception) {
                            Log.w(TAG, "Firebase Storage upload failed for photo $index: ${storageEx.message}, falling back to persistent cache")
                        }
                    }

                    // Always also save to local cache for instant offline rendering
                    val localFile = saveToInternalStorage(bytes, listingId, index)

                    if (!cloudUrl.isNullOrBlank()) {
                        uploadedUrls.add(cloudUrl)
                    } else if (localFile != null) {
                        uploadedUrls.add(Uri.fromFile(localFile).toString())
                    } else {
                        uploadedUrls.add(uri.toString())
                    }
                } else {
                    uploadedUrls.add(uri.toString())
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error processing image $index ($uri): ${e.message}")
                uploadedUrls.add(uri.toString())
            }
        }

        uploadedUrls
    }

    private fun compressAndRotateImageUri(uri: Uri): ByteArray? {
        return try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri) ?: return null

            // Read EXIF orientation before closing stream
            var orientation = ExifInterface.ORIENTATION_NORMAL
            try {
                val exifStream = context.contentResolver.openInputStream(uri)
                if (exifStream != null) {
                    val exifInterface = ExifInterface(exifStream)
                    orientation = exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    exifStream.close()
                }
            } catch (_: Exception) {}

            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            if (originalBitmap == null) return null

            // Apply EXIF rotation if necessary
            val rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(originalBitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(originalBitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(originalBitmap, 270f)
                else -> originalBitmap
            }

            // Scale down if larger than 1280x1280 to optimize bandwidth and fast preview
            val maxDimension = 1280
            val width = rotatedBitmap.width
            val height = rotatedBitmap.height
            val scaledBitmap = if (width > maxDimension || height > maxDimension) {
                val ratio = width.toFloat() / height.toFloat()
                val (newWidth, newHeight) = if (ratio > 1) {
                    Pair(maxDimension, (maxDimension / ratio).toInt())
                } else {
                    Pair((maxDimension * ratio).toInt(), maxDimension)
                }
                Bitmap.createScaledBitmap(rotatedBitmap, newWidth, newHeight, true)
            } else {
                rotatedBitmap
            }

            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            outputStream.toByteArray()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to compress image uri $uri: ${e.message}")
            null
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun saveToInternalStorage(bytes: ByteArray, listingId: String, index: Int): File? {
        return try {
            val dir = File(context.filesDir, "listing_photos").apply { if (!exists()) mkdirs() }
            val file = File(dir, "crop_${listingId}_${index}.jpg")
            FileOutputStream(file).use { it.write(bytes) }
            file
        } catch (e: Exception) {
            Log.w(TAG, "Failed to save image to internal storage: ${e.message}")
            null
        }
    }
}
