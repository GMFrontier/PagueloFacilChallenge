package com.gmfrontier.transaction_data.mapper

import com.gmfrontier.transaction_data.remote.dto.Transaction
import com.gmfrontier.transaction_domain.model.TransactionModel

fun Transaction.toUiTransactionModel(): TransactionModel {
    val status =
        if (messageSys.contains("PENDING"))
            "Pendiente"
        else if (messageSys.contains("VER"))
            "Aprobado"
        else "Cancelado"
    return TransactionModel(
        id = this.idTransaction,
        merchantName = this.merchantName,
        date = this.dateTms,
        codOper = this.codOper,
        cardHolderName = this.cardholderFullName,
        amount = this.amount,
        taxAmount = this.taxAmount,
        status = status,
        description = this.txDescription,
        cardType = this.cardType,
        isVisible = true
    )
}