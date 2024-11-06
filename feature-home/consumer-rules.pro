# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Keep all classes and members in the specified packages
-keep class com.waffiq.bazz_movies.feature.home.domain.model.** { *; }

-dontwarn com.waffiq.bazz_movies.feature.home.data.repository.HomeRepositoryImpl
-dontwarn com.waffiq.bazz_movies.feature.home.domain.repository.IHomeRepository
-dontwarn com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie.GetListMoviesInteractor
-dontwarn com.waffiq.bazz_movies.feature.home.domain.usecase.getListMovie.GetListMoviesUseCase
-dontwarn com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv.GetListTvInteractor
-dontwarn com.waffiq.bazz_movies.feature.home.domain.usecase.getListTv.GetListTvUseCase
-dontwarn com.waffiq.bazz_movies.feature.home.ui.FeaturedFragment_GeneratedInjector
-dontwarn com.waffiq.bazz_movies.feature.home.ui.HomeFragment_GeneratedInjector
-dontwarn com.waffiq.bazz_movies.feature.home.ui.MovieFragment_GeneratedInjector
-dontwarn com.waffiq.bazz_movies.feature.home.ui.TvSeriesFragment_GeneratedInjector
-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel
-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewmodel.MovieViewModel_HiltModules$KeyModule
-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel
-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewmodel.TvSeriesViewModel_HiltModules$KeyModule
-dontwarn com.waffiq.bazz_movies.feature.home.ui.viewpager.GenericViewPagerAdapter
-dontwarn com.waffiq.bazz_movies.feature.home.ui.FeaturedFragment
-dontwarn com.waffiq.bazz_movies.feature.home.ui.HomeFragment
-dontwarn com.waffiq.bazz_movies.feature.home.ui.MovieFragment
-dontwarn com.waffiq.bazz_movies.feature.home.ui.TvSeriesFragment