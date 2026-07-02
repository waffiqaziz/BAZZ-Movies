package com.waffiq.bazz_movies.feature.detail.utils.uihelpers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class CustomZoomAndPanTest {

  @MockK
  lateinit var pool: BitmapPool

  @MockK
  lateinit var sourceBitmap: Bitmap

  @MockK
  lateinit var resultBitmap: Bitmap

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
  }

  @After
  fun tearDown() {
    unmockkAll()
  }

  @Test
  fun transform_whenOutWidthIsZero_returnsOriginalBitmap() {
    val transform = CustomZoomAndPan(1.0f, 0.5f)
    val result = callTransform(transform, pool, sourceBitmap, 0, 100)
    assertSame(sourceBitmap, result)
    verify { pool wasNot Called }
  }

  @Test
  fun transform_whenOutHeightIsZero_returnsOriginalBitmap() {
    val transform = CustomZoomAndPan(1.0f, 0.5f)
    val result = callTransform(transform, pool, sourceBitmap, 100, 0)
    assertSame(sourceBitmap, result)
    verify { pool wasNot Called }
  }

  @Test
  fun transform_whenScaleYGreaterThanScaleX_scalesByHeightDrivenBaseScale() {
    // source 1000x200, target 100x100
    // scaleX = 100/1000 = 0.1, scaleY = 100/200 = 0.5 -> baseScale = 0.5
    every { sourceBitmap.width } returns 1000
    every { sourceBitmap.height } returns 200
    every { sourceBitmap.config } returns Bitmap.Config.ARGB_8888
    every { sourceBitmap.hasAlpha() } returns true
    every { pool.get(100, 100, Bitmap.Config.ARGB_8888) } returns resultBitmap
    every { resultBitmap.setHasAlpha(any()) } just Runs

    mockMatrixAndCanvas()

    val transform = CustomZoomAndPan(1.0f, 0.5f)
    val result = callTransform(transform, pool, sourceBitmap, 100, 100)

    assertSame(resultBitmap, result)
    verify { anyConstructed<Matrix>().setScale(0.5f, 0.5f) }

    // scaledWidth = 1000*0.5 = 500; dx = (100-500)*0.5 = -200
    verify { anyConstructed<Matrix>().postTranslate(-200f, any()) }
    verify { resultBitmap.setHasAlpha(true) }
  }

  @Test
  fun transform_whenConfigIsNull_fallsBackToArgb8888() {
    every { sourceBitmap.width } returns 100
    every { sourceBitmap.height } returns 100
    every { sourceBitmap.config } returns null
    every { sourceBitmap.hasAlpha() } returns false
    every { pool.get(50, 50, Bitmap.Config.ARGB_8888) } returns resultBitmap
    every { resultBitmap.setHasAlpha(any()) } just Runs

    mockMatrixAndCanvas()

    val transform = CustomZoomAndPan(1.0f, 0.5f)
    val result = callTransform(transform, pool, sourceBitmap, 50, 50)

    assertSame(resultBitmap, result)
    verify { pool.get(50, 50, Bitmap.Config.ARGB_8888) }
  }

  @Suppress("EqualsNullCall")
  @Test
  fun equals_whenOtherIsNull_returnsFalse() {
    val a = CustomZoomAndPan(1.0f, 0.5f)
    assertFalse(a.equals(null))
  }

  @Test
  fun equals_whenZoomFactorAndBiasXMatch_returnsTrue() {
    assertEquals(CustomZoomAndPan(1.3f, 0.25f), CustomZoomAndPan(1.3f, 0.25f))
  }

  @Test
  fun equals_whenBiasXDiffers_returnsFalse() {
    val a = CustomZoomAndPan(1.0f, 0.5f)
    val b = CustomZoomAndPan(1.0f, 0.0f)
    assertFalse(a == b)
  }

  @Test
  fun equals_whenZoomFactorAndBiasXBothDiffer_returnsFalse() {
    val a = CustomZoomAndPan(1.0f, 0.5f)
    val b = CustomZoomAndPan(2.0f, 1.0f)
    assertFalse(a == b)
  }

  // Reflection helper to invoke the protected `transform` methoddeclared on CustomZoomAndPan
  // (it overrides BitmapTransformation.transform, which is protected)
  private fun callTransform(
    instance: CustomZoomAndPan,
    pool: BitmapPool,
    bitmap: Bitmap,
    outWidth: Int,
    outHeight: Int,
  ): Bitmap {
    val method = instance.javaClass.getDeclaredMethod(
      "transform",
      BitmapPool::class.java,
      Bitmap::class.java,
      Int::class.javaPrimitiveType,
      Int::class.javaPrimitiveType,
    )
    method.isAccessible = true
    return method.invoke(instance, pool, bitmap, outWidth, outHeight) as Bitmap
  }

  private fun mockMatrixAndCanvas() {
    mockkConstructor(Matrix::class)
    every { anyConstructed<Matrix>().setScale(any(), any()) } just Runs
    every { anyConstructed<Matrix>().postTranslate(any(), any()) } returns true

    mockkConstructor(Canvas::class)
    every { anyConstructed<Canvas>().drawBitmap(any<Bitmap>(), any<Matrix>(), any()) } just Runs
  }
}
