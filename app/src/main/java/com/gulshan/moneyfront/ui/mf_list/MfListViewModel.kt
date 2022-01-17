package com.gulshan.moneyfront.ui.mf_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gulshan.moneyfront.data.MfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MfListViewModel @Inject constructor(repository: MfRepository) : ViewModel() {

    val mfs = repository.getAllMfs().cachedIn(viewModelScope)
}