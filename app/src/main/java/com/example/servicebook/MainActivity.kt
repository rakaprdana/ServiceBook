package com.example.servicebook

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servicebook.adapter.ListProductAdapter
import com.example.servicebook.adapter.PekerjaanAdapter
import com.example.servicebook.database.SQLDatabaseHandler
import com.example.servicebook.databinding.ActivityDashboardBinding
import com.example.servicebook.models.ListJobState
import com.example.servicebook.models.ListProduct

// Dashboard Service Books
class MainActivity : AppCompatActivity() {
    private lateinit var bindingDashboard: ActivityDashboardBinding
    private lateinit var adapterListProduct: ListProductAdapter
    private lateinit var adapterListJob: PekerjaanAdapter
    private var selectedListProduct: ListProduct = ListProduct.SEMUA_PRODUK
    private var selectedListState: ListJobState = ListJobState.NORMAL
    private var dbHandler: SQLDatabaseHandler = SQLDatabaseHandler(this)
    private var resultCode = 200

    // Launcher for handle result from AddJobActivity
    private val addJobLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (resultCode == 200) {
                selectedListState = ListJobState.NORMAL
                adapterListJob.setListState(selectedListState)
                refreshListProductAndJob(selectedListProduct)
                updateActionButtons()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingDashboard = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(bindingDashboard.root)

        setListProduct()
        setJobList()
        updateActionButtons()

        bindingDashboard.ivTambah.setOnClickListener {
            navigateToAddJob()
        }
        bindingDashboard.ivHapus.setOnClickListener {
            selectedListState = ListJobState.REMOVE
            adapterListJob.setListState(selectedListState)
            updateActionButtons()
        }
        bindingDashboard.btnBatal.setOnClickListener {
            selectedListState = ListJobState.NORMAL
            adapterListJob.setListState(selectedListState)
            updateActionButtons()
        }
    }

    private fun updateActionButtons() {
        val isEmpty = dbHandler.getJob().isEmpty()

        if (isEmpty && selectedListState == ListJobState.REMOVE) {
            selectedListState = ListJobState.NORMAL
            adapterListJob.setListState(selectedListState)
        }

        bindingDashboard.ivHapus.isVisible = selectedListState == ListJobState.NORMAL && !isEmpty
        bindingDashboard.btnBatal.isVisible = selectedListState == ListJobState.REMOVE
    }


    private fun setJobList() {
        adapterListJob =
            PekerjaanAdapter(
                dbHandler.getJob(), selectedListState
            ) { positionToBeRemove ->
                refreshAndRemove(positionToBeRemove)
            }
        bindingDashboard.rvListPekerjaan.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = adapterListJob
        }
    }

    private fun refreshAndRemove(position: Int) {
        dbHandler.deleteJob(position)
        adapterListJob.refreshList(dbHandler.getJob())
        updateActionButtons()
    }

    private fun navigateToAddJob() {
        val intent = Intent(this, AddJobActivity::class.java)
        addJobLauncher.launch(intent)
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