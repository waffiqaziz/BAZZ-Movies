package com.waffiq.bazz_movies.feature.list

import androidx.test.espresso.intent.Intents
import com.waffiq.bazz_movies.core.designsystem.R.string.all_time
import com.waffiq.bazz_movies.core.designsystem.R.string.costume_drama
import com.waffiq.bazz_movies.core.designsystem.R.string.donghua
import com.waffiq.bazz_movies.core.designsystem.R.string.reality_show
import com.waffiq.bazz_movies.core.designsystem.R.string.romance_drama
import com.waffiq.bazz_movies.core.designsystem.R.string.this_season
import com.waffiq.bazz_movies.core.instrumentationtest.CustomViewMatchers.isDisplayed
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isTextVisible
import com.waffiq.bazz_movies.core.instrumentationtest.CustomVisibilityMatchers.isVisible
import com.waffiq.bazz_movies.feature.list.R.id.collapse
import com.waffiq.bazz_movies.feature.list.testutils.BaseListActivityTest
import com.waffiq.bazz_movies.feature.list.ui.viewmodel.ListViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ListActivityArgsTest : BaseListActivityTest() {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @BindValue
  @JvmField
  val mockListViewModel: ListViewModel = mockk(relaxed = true)

  @Before
  override fun setup() {
    super.setup()
    hiltRule.inject()
    setupMock(mockListViewModel)
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  @Test
  fun listActivity_withGenreType_showsCorrectViews() {
    context.launchListActivity {
      collapse.isDisplayed()
      "Science Fiction".isVisible()
      shouldShowMovie()
    }
    context.launchListActivity(tvGenreArgs) {
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withKeywordsType_showsCorrectViews() {
    context.launchListActivity(movieKeywordsArgs) {
      "Post Apocalyptic".isVisible()
      shouldShowMovie()
    }
    context.launchListActivity(tvKeywordsArgs) {
      "Post Apocalyptic".isVisible()
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withNowPlayingType_showsCorrectViews() {
    context.launchListActivity(movieNowPlayingArgs) {
      shouldShowMovie()
    }
    context.launchListActivity(tvNowPlayingArgs) {
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withPopularType_showsCorrectViews() {
    context.launchListActivity(moviePopularArgs) {
      shouldShowMovie()
    }
    context.launchListActivity(tvPopularArgs) {
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withRecommendationType_showsCorrectViews() {
    context.launchListActivity(movieRecommendationArgs) {
      movieRecommendationArgs.title.isVisible()
      "Recommendation".isVisible()
    }
    context.launchListActivity(tvRecommendationArgs) {
      tvRecommendationArgs.title.isVisible()
      "Recommendation".isVisible()
    }
  }

  @Test
  fun listActivity_withTopRatedType_showsCorrectViews() {
    context.launchListActivity(movieTopRatedArgs) {
      shouldShowMovie()
    }
    context.launchListActivity(tvTopRatedArgs) {
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withUpcomingType_showsCorrectViews() {
    context.launchListActivity(movieUpcomingArgs) {
      shouldShowMovie()
    }
  }

  @Test
  fun listActivity_withAiringThisWeekType_showsCorrectViews() {
    context.launchListActivity(tvAiringThisWeekArgs) {
      shouldShowTv()
    }
  }

  @Test
  fun listActivity_withTrendingTodayType_showsCorrectViews() {
    context.launchListActivity(trendingTodayArgs) {
      shouldShowTrending()
    }
  }

  @Test
  fun listActivity_withTrendingThisWeekType_showsCorrectViews() {
    context.launchListActivity(trendingThisWeekArgs) {
      shouldShowTrending()
    }
  }

  @Test
  fun listActivity_withAnimeAllTimeType_showsCorrectViews() {
    context.launchListActivity(animeAllTimeArgs) {
      all_time.isTextVisible()
    }
  }

  @Test
  fun listActivity_withAnimeThisSeasonType_showsCorrectViews() {
    context.launchListActivity(animeThisSeasonArgs) {
      this_season.isTextVisible()
    }
  }

  @Test
  fun listActivity_withCostumeDramaType_showsCorrectViews() {
    context.launchListActivity(costumeDramaArgs) {
      costume_drama.isTextVisible()
    }
  }

  @Test
  fun listActivity_withDonghuaType_showsCorrectViews() {
    context.launchListActivity(donghuaArgs) {
      donghua.isTextVisible()
    }
  }

  @Test
  fun listActivity_withRomanceDramaType_showsCorrectViews() {
    context.launchListActivity(romanceDramaArgs) {
      romance_drama.isTextVisible()
    }
  }

  @Test
  fun listActivity_withRealityShowType_showsCorrectViews() {
    context.launchListActivity(realityShow) {
      reality_show.isTextVisible()
    }
  }
}
