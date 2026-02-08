package com.waffiq.bazz_movies.core.instrumentationtest

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.waffiq.bazz_movies.core.designsystem.R.style.Base_Theme_BAZZ_movies

/**
 * Replacement for `launchFragmentInContainer` which is not usable here due to potential
 * circular dependency with the `:app` module that contains `MyApplication`.
 *
 * This function launches a fragment inside a [HiltTestActivity], which supports Hilt injection.
 * It can be used in instrumented tests shared across modules.
 *
 * @param fragmentArgs Optional arguments to pass to the fragment.
 * @param themeResId Theme to apply to the activity.
 * @param fragmentFactory Optional custom [FragmentFactory] to use for instantiating the fragment.
 * @param action Lambda to execute on the fragment after it's launched.
 */
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
  fragmentArgs: Bundle? = null,
  @StyleRes themeResId: Int = Base_Theme_BAZZ_movies,
  fragmentFactory: FragmentFactory? = null,
  crossinline action: T.() -> Unit = {},
): T {
  lateinit var fragmentInstance: T

  val startActivityIntent = Intent.makeMainActivity(
    ComponentName(
      ApplicationProvider.getApplicationContext(),
      HiltTestActivity::class.java,
    ),
  ).putExtra(
    "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
    themeResId,
  )

  ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
    fragmentFactory?.let {
      activity.supportFragmentManager.fragmentFactory = it
    }

    val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
      requireNotNull(T::class.java.classLoader),
      T::class.java.name,
    ).apply {
      arguments = fragmentArgs
    }

    activity.supportFragmentManager
      .beginTransaction()
      .add(android.R.id.content, fragment, null)
      .commitNow()

    (fragment as T).action()
    fragmentInstance = fragment
  }

  return fragmentInstance
}
