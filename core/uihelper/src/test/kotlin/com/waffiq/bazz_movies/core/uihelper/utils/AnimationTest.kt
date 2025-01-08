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
  fun `fadeOut should animate alpha to 0 and set view to invisible on animation end`() {
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
  fun `fadeInAlpha50 should animate alpha to 50 percent and set view to visible`() {
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
  fun `fadeOut should handle negative duration gracefully`() {
    Animation.fadeOut(mockView, -100L)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
    verify { mockAnimator.duration = 300L } // assume default duration is used
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun `fadeOut should use default duration gracefully`() {
    Animation.fadeOut(mockView)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
    verify { mockAnimator.duration = 300L }
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun `fadeOut should handle zero duration`() {
    Animation.fadeOut(mockView, 0L)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
    verify { mockAnimator.duration = 0L }
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun `fadeOut should handle already invisible view`() {
    every { mockView.isVisible } returns false

    Animation.fadeOut(mockView)

    // ensure animation is still called
    verify { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
  }

  @Test
  fun `fadeInAlpha50 should handle negative duration gracefully`() {
    Animation.fadeInAlpha50(mockView, -200L)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0.5f) }
    verify { mockAnimator.duration = 300L }
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun `fadeInAlpha50 should use default duration gracefully`() {
    Animation.fadeInAlpha50(mockView)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0.5f) }
    verify { mockAnimator.duration = 300L }
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun `fadeInAlpha50 should handle zero duration`() {
    Animation.fadeInAlpha50(mockView, 0L)

    verify { mockView.animate() }
    verify { mockAnimator.alpha(0.5f) }
    verify { mockAnimator.duration = 0L }
    verify { mockAnimator.setListener(any()) }
  }

  @Test
  fun `fadeInAlpha50 should handle already visible view`() {
    every { mockView.isVisible } returns true
    every { mockView.alpha } returns 0.5f

    Animation.fadeInAlpha50(mockView, 300L)

    // ensure animation is called but visibility remains true
    verify { mockView.animate() }
    verify { mockAnimator.alpha(0.5f) }
    verify { mockView.isVisible = true }
  }

  @Test
  fun `fadeOut and fadeInAlpha50 called concurrently`() {
    Animation.fadeOut(mockView, 300L)
    Animation.fadeInAlpha50(mockView, 300L)

    verify(exactly = 2) { mockView.animate() }
    verify { mockAnimator.alpha(0f) }
    verify { mockAnimator.alpha(0.5f) }
  }
}
