## Glide
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep class * extends com.bumptech.glide.module.AppGlideModule {
# <init>(...);
#}
#-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
#-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
#  *** rewind();
#}
#
#-dontwarn com.waffiq.bazz_movies.feature.search.data.repository.SearchRepositoryImpl
#-dontwarn com.waffiq.bazz_movies.feature.search.domain.repository.ISearchRepository
#-dontwarn com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchInteractor
#-dontwarn com.waffiq.bazz_movies.feature.search.domain.usecase.MultiSearchUseCase
#-dontwarn com.waffiq.bazz_movies.feature.search.ui.SearchFragment
#-dontwarn com.waffiq.bazz_movies.feature.search.ui.SearchFragment_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.search.ui.SearchViewModel
#-dontwarn com.waffiq.bazz_movies.feature.search.ui.SearchViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.search.ui.SearchFragment_MembersInjector