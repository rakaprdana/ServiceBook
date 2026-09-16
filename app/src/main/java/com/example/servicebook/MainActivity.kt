package com.example.servicebook

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servicebook.adapter.ListProductAdapter
import com.example.servicebook.adapter.PekerjaanAdapter
import com.example.servicebook.database.SQLDatabaseHandler
import com.example.servicebook.databinding.ActivityDashboardBinding
import com.example.servicebook.models.ListProduct

// Dashboard Service Books
class MainActivity : AppCompatActivity() {
    private lateinit var bindingDashboard: ActivityDashboardBinding
    private lateinit var adapterListProduct: ListProductAdapter
    private lateinit var adapterListJob: PekerjaanAdapter
    private var selectedListProduct: ListProduct = ListProduct.SEMUA_PRODUK
    private var dbHandler: SQLDatabaseHandler = SQLDatabaseHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingDashboard = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(bindingDashboard.root)

        setListProduct()

    }

    fun setListProduct() {
        val listProduct = ListProduct.values().toList()
        adapterListProduct = ListProductAdapter(listProduct, selectedListProduct) { listProduct ->
            selectedListProduct = listProduct
            refreshListProductAndJob(listProduct)
        }

        bindingDashboard.rvListProduk.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = adapterListProduct
        }
    }

    private fun refreshListProductAndJob(listProduct: ListProduct) {
        val listJob = if (listProduct == ListProduct.SEMUA_PRODUK) {
            dbHandler.getJob()
        } else {
            dbHandler.getJob().filter { it.listProduct == listProduct }
        }

        adapterListJob.refreshList(listJob)
        adapterListProduct.updateSelectedListProduct(selectedListProduct)
    }
}