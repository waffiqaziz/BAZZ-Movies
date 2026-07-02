package com.waffiq.bazz_movies.feature.detail.utils.uihelpers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class CustomZoomAndPan(
  private val zoomFactor: Float, // 1.0 = normal crop, 1.3 = 30% zoomed in
  private val biasX: Float, // 0.0 = Left, 0.5 = Center, 1.0 = Far Right
) : BitmapTransformation() {

  override fun transform(
    pool: BitmapPool,
    toTransform: Bitmap,
    outWidth: Int,
    outHeight: Int,
  ): Bitmap {
    if (outWidth <= 0 || outHeight <= 0) return toTransform

    val scaleX = outWidth.toFloat() / toTransform.width.toFloat()
    val scaleY = outHeight.toFloat() / toTransform.height.toFloat()
    val baseScale = scaleX.coerceAtLeast(scaleY)
    val finalScale = baseScale * zoomFactor

    val scaledWidth = toTransform.width * finalScale
    val scaledHeight = toTransform.height * finalScale

    val dx = (outWidth - scaledWidth) * biasX
    val dy = (outHeight - scaledHeight) * CENTERED

    val matrix = Matrix()
    matrix.setScale(finalScale, finalScale)
    matrix.postTranslate(dx, dy)

    val config = toTransform.config ?: Bitmap.Config.ARGB_8888
    val result = pool.get(outWidth, outHeight, config)
    result.setHasAlpha(toTransform.hasAlpha())

    val canvas = Canvas(result)
    canvas.drawBitmap(toTransform, matrix, Paint(Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG))

    return result
  }

  override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    messageDigest.update("custom_zoom_pan_${zoomFactor}_$biasX".toByteArray(Charsets.UTF_8))
  }

  override fun equals(other: Any?): Boolean {
    if (other is CustomZoomAndPan) {
      return zoomFactor == other.zoomFactor && biasX == other.biasX
    }
    return false
  }

  override fun hashCode(): Int = zoomFactor.hashCode() * 31 + biasX.hashCode()

  companion object {
    private const val CENTERED = 0.5f
  }
}
