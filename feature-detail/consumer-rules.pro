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
-keep class com.waffiq.bazz_movies.feature.detail.domain.model.** { *; }

# This is generated automatically by the Android Gradle plugin.
-dontwarn com.waffiq.bazz_movies.feature.detail.data.repository.DetailRepositoryImpl
-dontwarn com.waffiq.bazz_movies.feature.detail.domain.repository.IDetailRepository
-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie.GetDetailMovieInteractor
-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailMovie.GetDetailMovieUseCase
-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb.GetDetailOMDbInteractor
-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailOmdb.GetDetailOMDbUseCase
-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv.GetDetailTvInteractor
-dontwarn com.waffiq.bazz_movies.feature.detail.domain.usecase.getDetailTv.GetDetailTvUseCase
-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailMovieActivity_GeneratedInjector
-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailMovieViewModel
-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailMovieViewModel_HiltModules$KeyModule
-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailUserPrefViewModel
-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailUserPrefViewModel_HiltModules$KeyModule
-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailMovieActivity
-dontwarn com.waffiq.bazz_movies.feature.detail.ui.DetailMovieActivity_MembersInjector