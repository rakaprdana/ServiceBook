package com.example.servicebook.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.servicebook.R
import com.example.servicebook.databinding.ItemListProductBinding
import com.example.servicebook.models.ListProduct

class ListProductAdapter(
    private val mList: List<ListProduct>,
    private var selectedListProduct: ListProduct,
    private var onSelectedProduct: (ListProduct) -> Unit
) : RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder>() {
    private var currentListProduct: ListProduct = selectedListProduct
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListProductViewHolder {
        val view = ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListProductViewHolder(view)
    }
    // Function for updating color state list product
    internal fun updateSelectedListProduct(selectedListProduct: ListProduct){
        notifyItemChanged(currentListProduct.ordinal)
        currentListProduct = selectedListProduct
        notifyItemChanged(selectedListProduct.ordinal)
    }

    override fun onBindViewHolder(
        holder: ListProductViewHolder,
        position: Int
    ) {
        holder.bin(mList[position], currentListProduct, onSelectedProduct)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ListProductViewHolder(private val itemListProductBinding: ItemListProductBinding) :
        RecyclerView.ViewHolder(itemListProductBinding.root) {
        fun bin(
            item: ListProduct,
            selectedListProduct: ListProduct,
            onSelectedProduct: (ListProduct) -> Unit
        ) {
            itemListProductBinding.tvListProduct.text = item.nameProduct
            if (item.ordinal == selectedListProduct.ordinal) {
                itemListProductBinding.border.setCardBackgroundColor(
                    itemListProductBinding.root.context.getColor(
                        R.color.mainColor
                    )
                )
            } else {
                itemListProductBinding.border.setCardBackgroundColor(
                    itemListProductBinding.root.context.getColor(
                        R.color.colorCardBackground
                    )
                )
            }
            itemListProductBinding.root.setOnClickListener {
                onSelectedProduct(item)
            }
        }
    }
}