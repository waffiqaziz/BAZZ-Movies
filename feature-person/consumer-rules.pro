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
## Keep all classes and members in the specified packages
#-keep class com.waffiq.bazz_movies.feature.person.domain.model.** { *; }
#
#-dontwarn com.waffiq.bazz_movies.feature.person.data.repository.PersonRepositoryImpl
#-dontwarn com.waffiq.bazz_movies.feature.person.domain.repository.IPersonRepository
#-dontwarn com.waffiq.bazz_movies.feature.person.domain.usecase.GetDetailPersonInteractor
#-dontwarn com.waffiq.bazz_movies.feature.person.domain.usecase.GetDetailPersonUseCase
#-dontwarn com.waffiq.bazz_movies.feature.person.ui.PersonActivity_GeneratedInjector
#-dontwarn com.waffiq.bazz_movies.feature.person.ui.PersonViewModel
#-dontwarn com.waffiq.bazz_movies.feature.person.ui.PersonViewModel_HiltModules$KeyModule
#-dontwarn com.waffiq.bazz_movies.feature.person.ui.PersonActivity
#-dontwarn com.waffiq.bazz_movies.feature.person.ui.PersonActivity_MembersInjector