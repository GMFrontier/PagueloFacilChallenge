package com.gmfrontier.transaction_presentation.home

import com.example.core.util.UiText
import com.gmfrontier.transaction_domain.model.TransactionModel
import com.gmfrontier.transaction_presentation.util.Event

data class HomeState(
    val isSearching: Event<Boolean>? = null,
    val transactions: Event<List<TransactionModel>>? = null,
    val showMessage: Event<UiText>? = null,
    val scrollToTop: Event<Boolean>? = null
)