package com.waffiq.bazz_movies.core.instrumentationtest

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * A test-only [AppCompatActivity] annotated with [@AndroidEntryPoint] to support launching
 * Hilt-enabled fragments in instrumented UI tests.
 *
 * This activity acts as a host for fragments in shared testing scenarios using
 * [launchFragmentInHiltContainer], and is required for proper Hilt injection during UI tests.
 *
 * ## Usage
 * To use this activity in any module’s instrumented tests (e.g., `:feature:search`),
 * it must be declared in the `AndroidManifest.xml` under the `androidTest` source set of that module:
 *
 * ```xml
 * <manifest xmlns:android="http://schemas.android.com/apk/res/android">
 *   <application android:theme="@style/Base.Theme.BAZZ_movies">
 *     <activity
 *       android:name="com.waffiq.bazz_movies.core.instrumentationtest.HiltTestActivity"
 *       android:exported="true"
 *       android:windowSoftInputMode="stateHidden"
 *       android:autofillHints="" />
 *   </application>
 * </manifest>
 * ```
 *
 * This approach allows you to reuse `HiltTestActivity` across modules without introducing
 * circular dependencies with the `:app` module.
 *
 * @see launchFragmentInHiltContainer
 */
@AndroidEntryPoint
class HiltTestActivity : AppCompatActivity()
