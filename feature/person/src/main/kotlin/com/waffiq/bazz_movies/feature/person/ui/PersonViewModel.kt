package com.waffiq.bazz_movies.feature.person.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_movies.core.uihelper.state.UIState
import com.waffiq.bazz_movies.core.uihelper.state.asUiState
import com.waffiq.bazz_movies.feature.person.domain.model.CastItem
import com.waffiq.bazz_movies.feature.person.domain.model.DetailPerson
import com.waffiq.bazz_movies.feature.person.domain.model.ProfilesItem
import com.waffiq.bazz_movies.feature.person.domain.usecase.GetDetailPersonUseCase
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.mapCastList
import com.waffiq.bazz_movies.feature.person.utils.mapper.PersonMapper.mapImageList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
  private val getDetailPersonUseCase: GetDetailPersonUseCase,
) : ViewModel() {

  private val personId = MutableStateFlow<Int?>(null)

  val detailPersonState: StateFlow<UIState<DetailPerson>> = personId
    .filterNotNull()
    .flatMapLatest { id -> getDetailPersonUseCase.getDetailPerson(id).asUiState() }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIME), UIState.Idle)

  val castList: StateFlow<List<CastItem>> = detailPersonState
    .map(::mapCastList)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIME), emptyList())

  val imageList: StateFlow<List<ProfilesItem>> = detailPersonState
    .map(::mapImageList)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIME), emptyList())

  fun getDetailPerson(id: Int) {
    personId.value = id
  }

  private companion object {
    const val TIME = 5_000L
  }
}
