package com.example.servicebook.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.servicebook.databinding.ItemListPekerjaanBinding
import com.example.servicebook.models.ListJobState
import com.example.servicebook.models.PekerjaanData
import java.time.format.DateTimeFormatter
import java.util.Locale

class PekerjaanAdapter(
    mList: List<PekerjaanData>,
    selectedListState: ListJobState,
    private val onRemoveJob: (Int) -> Unit,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<PekerjaanAdapter.PekerjaanViewHolder>() {
    private var currentListJob = mList
    private var currentListState = selectedListState
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PekerjaanViewHolder {
        val view =
            ItemListPekerjaanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PekerjaanViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PekerjaanViewHolder,
        position: Int
    ) {
        holder.bind(currentListJob[position], currentListState, onRemoveJob, onItemClick)
    }

    override fun getItemCount(): Int {
        return currentListJob.size
    }


    class PekerjaanViewHolder(private val itemJobViewBinding: ItemListPekerjaanBinding) :
        RecyclerView.ViewHolder(itemJobViewBinding.root) {
        fun bind(
            item: PekerjaanData,
            currentLisState: ListJobState,
            onRemoveJob: (Int) -> Unit,
            onItemClick: (Int) -> Unit
        ) {
            val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))
            itemJobViewBinding.tvNama.text = item.nameClient
            itemJobViewBinding.tvKategori.apply {
                text = item.listProduct?.nameProduct
                requestLayout()
            }
            itemJobViewBinding.tvTanggal.text = item.date?.format(dateFormatter)
            itemJobViewBinding.tvStatus.apply {
                text = item.status.name
                requestLayout()
            }

            itemJobViewBinding.layoutStatus.setCardBackgroundColor(
                itemJobViewBinding.root.context.getColor(
                    item.status.color
                )
            )
            itemJobViewBinding.btnRemove.isVisible = currentLisState == ListJobState.REMOVE
            itemJobViewBinding.btnRemove.setOnClickListener {
                onRemoveJob(item.id)
            }

            itemJobViewBinding.root.setOnClickListener {
                if (currentLisState == ListJobState.NORMAL) {
                    onItemClick(item.id)
                }
            }
        }
    }

    internal fun setListState(selectedListState: ListJobState){
        currentListState = selectedListState
        notifyDataSetChanged()
    }

    internal fun refreshList(list: List<PekerjaanData>){
        currentListJob = list
        notifyDataSetChanged()
    }
}
