package com.gmfrontier.transaction_presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.util.UiText
import com.gmfrontier.transaction_domain.model.TransactionModel
import com.gmfrontier.transaction_domain.repository.QUERY_PAGE_SIZE
import com.gmfrontier.transaction_domain.use_case.GetTransactionsCount
import com.gmfrontier.transaction_domain.use_case.SearchTransactions
import com.gmfrontier.transaction_presentation.R
import com.gmfrontier.transaction_presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchTransactions: SearchTransactions,
    private val getTransactionsCount: GetTransactionsCount
) : ViewModel() {

    private val _state = MutableLiveData<HomeState>()
    val state: LiveData<HomeState>
        get() = _state
    private var transactionsCache: List<TransactionModel> = emptyList()
    private var token = ""

    var totalItems = 0
    var actualPage = 1
    var isLastPage = false


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnSearch -> {
                viewModelScope.launch {
                    sendState(isSearching = Event(true))
                    getTransactionsCount(token)
                        .onSuccess {
                            totalItems = it
                            actualPage = 1
                            searchTransactions(offset = 0, operation = event.codOper, token = token)
                                .onSuccess {
                                    actualPage++
                                    val totalPages = totalItems / QUERY_PAGE_SIZE + 2
                                    isLastPage = actualPage >= totalPages
                                    if (it.isEmpty()) {
                                        sendState(
                                            isSearching = Event(false),
                                            showMessage = Event(UiText.StringResource(R.string.no_results))
                                        )
                                    } else {
                                        transactionsCache = it
                                        sendState(
                                            isSearching = Event(false),
                                            transactions = Event(transactionsCache)
                                        )
                                    }
                                }
                                .onFailure {
                                    sendState(showMessage = Event(UiText.StringResource(R.string.error_unexpected_error)))
                                }
                        }
                }
            }
            is HomeEvent.OnPaginate -> {
                viewModelScope.launch {
                    sendState(isSearching = Event(true))
                    delay(500)
                    searchTransactions(event.offset, token = token)
                        .onSuccess {
                            actualPage++
                            val totalPages = totalItems / QUERY_PAGE_SIZE + 2
                            isLastPage = actualPage >= totalPages
                            if (it.isEmpty()) {
                                sendState(
                                    isSearching = Event(false),
                                    showMessage = Event(UiText.StringResource(R.string.no_more_results))
                                )
                            } else {
                                transactionsCache = transactionsCache + it
                                sendState(
                                    isSearching = Event(false),
                                    transactions = Event(transactionsCache)
                                )
                            }
                        }
                        .onFailure {
                            sendState(showMessage = Event(UiText.StringResource(R.string.error_unexpected_error)))
                        }

                }
            }
            is HomeEvent.OnSortByDateSwitch -> {
                viewModelScope.launch {
                    if (event.isAscending) {
                        transactionsCache = transactionsCache.sortedBy { it.date }
                        sendState(
                            scrollToTop = Event(true),
                            transactions = Event(transactionsCache)
                        )
                    } else {
                        transactionsCache = transactionsCache.sortedByDescending { it.date }
                        sendState(
                            scrollToTop = Event(true),
                            transactions = Event(transactionsCache)
                        )
                    }
                }
            }
            is HomeEvent.OnFilterList -> {
                viewModelScope.launch {
                    if (event.filter.isNotBlank()) {
                        val cardType = when (event.filter.toInt()) {
                            0 -> "VISA"
                            1 -> "MC"
                            2 -> "CASH"
                            3 -> "CLAVE"
                            else -> "CASH"
                        }
                        transactionsCache = transactionsCache.map {
                            it.copy(isVisible = it.cardType == cardType)
                        }
                        sendState(
                            transactions = Event(transactionsCache)
                        )
                    } else {
                        transactionsCache = transactionsCache.map { it.copy(isVisible = true) }
                        sendState(
                            transactions = Event(transactionsCache)
                        )
                    }
                }
            }
            is HomeEvent.OnTokenArrived -> token = event.token
        }
    }

    private suspend fun sendState(
        isSearching: Event<Boolean>? = null,
        transactions: Event<List<TransactionModel>>? = null,
        showMessage: Event<UiText>? = null,
        scrollToTop: Event<Boolean>? = null
    ) = withContext(Dispatchers.Main) {
        _state.value = HomeState(
            isSearching = isSearching,
            transactions = transactions,
            showMessage = showMessage,
            scrollToTop = scrollToTop,
        )
    }
}