package com.waffiq.bazz_movies.core.uihelper.utils

import android.animation.Animator
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.isVisible
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AnimationTest {

  private lateinit var mockView: View
  private lateinit var mockAnimator: ViewPropertyAnimator

  @Before
  fun setUp() {
    mockView = mockk(relaxed = true)
    mockAnimator = mockk(relaxed = true)

    every { mockView.animate() } returns mockAnimator
    every { mockAnimator.alpha(any()) } returns mockAnimator
    every { mockAnimator.setDuration(any()) } returns mockAnimator
    every { mockAnimator.setListener(any()) } returns mockAnimator
  }

  @Test
  fun fadeOut_whenAnimateAlphaToZero_setViewInvisibleOnAnimationEnd() {
    val duration = 300L

    val animationListenerSlot = slot<Animator.AnimatorListener>()
    every { mockAnimator.setListener(capture(animationListenerSlot)) } returns mockAnimator

    Animation.fadeOut(mockView, duration)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
    verify { mockAnimator.duration = duration }
    verify { mockAnimator.setListener(any()) }

    // simulate animation end
    animationListenerSlot.captured.onAnimationEnd(mockk())
    verify { mockView.isVisible = false }
  }

  @Test
  fun fadeInAlpha50_whenCalled_setViewToVisible() {
    val duration = 300L
    val fadeAlpha = 0.5f

    Animation.fadeInAlpha50(mockView, duration)

    verify { mockView.alpha = 0f }
    verify { mockView.isVisible = true }
    verify { mockView.animate() }
    verify { mockAnimator.alpha(fadeAlpha) }
    verify { mockAnimator.duration = duration }
    verify { mockAnimator.setListener(null) }
  }

  // edge case
  @Test
  fun fadeOut_whenDurationIsNegative_useDefaultDuration() {
    Animation.fadeOut(mockView, -100L)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
    verify { mockAnimator.duration = 300L } // assume default duration is used
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun fadeOut_whenUseDefaultDuration_useDefaultDuration() {
    Animation.fadeOut(mockView)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
    verify { mockAnimator.duration = 300L }
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun fadeOut_whenUseZeroDuration_shouldUseZeroDuration() {
    Animation.fadeOut(mockView, 0L)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
    verify { mockAnimator.duration = 0L }
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun fadeOut_whenViewIsInvisible_stillAnimatesAlpha() {
    every { mockView.isVisible } returns false

    Animation.fadeOut(mockView)

    // ensure animation is still called
    verify { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
  }

  @Test
  fun fadeInAlpha50_withNegativeDuration_useDefaultDuration() {
    Animation.fadeInAlpha50(mockView, -200L)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0.5f) }
    verify { mockAnimator.duration = 300L }
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun fadeInAlpha50_whenUseDefaultDuration_useDefaultDuration() {
    Animation.fadeInAlpha50(mockView)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0.5f) }
    verify { mockAnimator.duration = 300L }
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun fadeInAlpha50_whenUseZeroDuration_useZeroDuration() {
    Animation.fadeInAlpha50(mockView, 0L)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0.5f) }
    verify { mockAnimator.duration = 0L }
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun fadeInAlpha50_whenViewIsVisible_stillAnimate() {
    every { mockView.isVisible } returns true
    every { mockView.alpha } returns 0.5f

    Animation.fadeInAlpha50(mockView, 300L)

    // ensure animation is called but visibility remains true
    verify { mockView.animate() }
    verify { mockAnimator.alpha(0.5f) }
    verify { mockView.isVisible = true }
  }

  @Test
  fun fadeOutAndFadeInAlpha50_whenCalledConcurrently_animationUpdateCorrectly() {
    Animation.fadeOut(mockView, 300L)
    Animation.fadeInAlpha50(mockView, 300L)

    verify(exactly = 2) { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
    verify { mockAnimator.alpha(0.5f) }
  }
}
