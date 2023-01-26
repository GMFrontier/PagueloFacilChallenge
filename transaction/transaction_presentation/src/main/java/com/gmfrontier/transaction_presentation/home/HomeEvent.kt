package com.gmfrontier.transaction_presentation.home

sealed class HomeEvent {
    data class OnSearch(val codOper: String) : HomeEvent()
    data class OnPaginate(val offset: Int) : HomeEvent()
    data class  OnSortByDateSwitch(val isAscending: Boolean) : HomeEvent()
    data class  OnFilterList(val filter: String) : HomeEvent()
    data class  OnTokenArrived(val token: String) : HomeEvent()
}
