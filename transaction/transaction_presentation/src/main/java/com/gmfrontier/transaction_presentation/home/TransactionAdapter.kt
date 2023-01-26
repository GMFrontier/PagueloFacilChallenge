package com.gmfrontier.transaction_presentation.home

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmfrontier.transaction_domain.model.CASH
import com.gmfrontier.transaction_domain.model.MASTERCARD
import com.gmfrontier.transaction_domain.model.TransactionModel
import com.gmfrontier.transaction_presentation.R
import com.gmfrontier.transaction_presentation.databinding.ListItemTransactionBinding

class TransactionAdapter(
) : ListAdapter<TransactionModel, TransactionAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(transaction: TransactionModel) {

            val holderName = if(transaction.cardHolderName.isBlank()) itemView.context.getString(R.string.status_cancelled) else transaction.cardHolderName
            val textColor =
                if (transaction.status.contains(itemView.context.getString(R.string.status_pending)))
                    itemView.context.getColor(
                        com.gmfrontier.core_ui.R.color.pendingSatusColor
                    )
                else if (transaction.status.contains(itemView.context.getString(R.string.status_approved)))
                    itemView.context.getColor(com.gmfrontier.core_ui.R.color.primaryColor)
                else itemView.context.getColor(
                    com.gmfrontier.core_ui.R.color.canceledSatusColor
                )
            val cardType = when (
                transaction.cardType) {
                CASH -> itemView.context.getString(R.string.cash)
                MASTERCARD -> itemView.context.getString(R.string.mastercard)
                else -> transaction.cardType
            }
            binding.btnExpand.setOnClickListener { expandList() }
            binding.tvMerchantName.text = transaction.merchantName
            binding.tvOperation.text =
                itemView.context.getString(R.string.operation_code, transaction.codOper)
            binding.tvDate.text = transaction.date
            binding.tvHolderName.text = itemView.context.getString(
                R.string.card_holder_name,
                holderName
            )
            binding.tvApproved.text =
                itemView.context.getString(R.string.approved, transaction.status)
            binding.tvApproved.setTextColor(textColor)
            binding.tvAmount.text =
                itemView.context.getString(R.string.amount, transaction.amount.toString())
            binding.tvTaxAmount.text = itemView.context.getString(
                R.string.tax_amount,
                transaction.taxAmount.toString()
            )
            binding.tvDescription.text = transaction.description
            binding.tvCardType.text = itemView.context.getString(
                R.string.card_type,
                cardType
            )
        }

        fun expandList() {
            TransitionManager.beginDelayedTransition((binding.root as ViewGroup), AutoTransition())
            binding.btnExpand.isActivated = !binding.btnExpand.isActivated
            binding.clExpanded.visibility =
                if (binding.btnExpand.isActivated) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(currentList[holder.adapterPosition])
    }

    class DiffCallback : DiffUtil.ItemCallback<TransactionModel>() {
        override fun areItemsTheSame(
            oldItem: TransactionModel,
            newItem: TransactionModel
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.isVisible == newItem.isVisible
        }

        override fun areContentsTheSame(
            oldItem: TransactionModel,
            newItem: TransactionModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}