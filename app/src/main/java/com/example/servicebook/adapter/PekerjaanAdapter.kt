package com.example.servicebook.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.servicebook.databinding.ItemListPekerjaanBinding
import com.example.servicebook.models.ListJobState
import com.example.servicebook.models.PekerjaanData

class PekerjaanAdapter(
    mList: List<PekerjaanData>,
    selectedListState: ListJobState,
    private val onRemoveJob: (Int) -> Unit
) : RecyclerView.Adapter<PekerjaanAdapter.PekerjaanViewHolder>() {
    private var currentListJob = mList
    private var currentListState = selectedListState


    class PekerjaanViewHolder(private val itemJobViewBinding: ItemListPekerjaanBinding) :
        RecyclerView.ViewHolder(itemJobViewBinding.root) {
        fun bind(item: PekerjaanData, currentLisState: ListJobState, onRemoveJob: (Int) -> Unit) {
            itemJobViewBinding.tvNama.text = item.nameClient
            itemJobViewBinding.tvKategori.apply {
                text = item.listProduct?.nameProduct
                requestLayout()
            }
            itemJobViewBinding.tvStatus.apply {
                text = item.status.name
                requestLayout()
            }

            itemJobViewBinding.layoutStatus.setCardBackgroundColor(
                itemJobViewBinding.root.context.getColor(
                    item.status.color
                )
            )
            itemJobViewBinding.btnRemove.isVisible = currentLisState== ListJobState.REMOVE
            itemJobViewBinding.btnRemove.setOnClickListener {
                onRemoveJob(item.id)
            }

        }
    }
}