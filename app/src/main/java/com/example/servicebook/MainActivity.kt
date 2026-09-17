package com.example.servicebook

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
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
    private var searchQuery: String = ""
    private var dbHandler: SQLDatabaseHandler = SQLDatabaseHandler(this)
    private var resultCode = 200

    // Launcher for handle result from AddJobActivity
    @RequiresApi(Build.VERSION_CODES.O)
    private val addJobLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (resultCode == 200) {
                selectedListState = ListJobState.NORMAL
                adapterListJob.setListState(selectedListState)
                refreshListProductAndJob()
                updateActionButtons()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingDashboard = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(bindingDashboard.root)

        setListProduct()
        setJobList()
        setSearchFunction()
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        refreshListProductAndJob()
        updateActionButtons()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateActionButtons() {
        val isEmpty = dbHandler.getJob().isEmpty()

        if (isEmpty && selectedListState == ListJobState.REMOVE) {
            selectedListState = ListJobState.NORMAL
            adapterListJob.setListState(selectedListState)
        }

        bindingDashboard.ivHapus.isVisible = selectedListState == ListJobState.NORMAL && !isEmpty
        bindingDashboard.btnBatal.isVisible = selectedListState == ListJobState.REMOVE
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setJobList() {
        adapterListJob =
            PekerjaanAdapter(
                dbHandler.getJob(), selectedListState,
                onRemoveJob = { positionToBeRemove ->
                    refreshAndRemove(positionToBeRemove)
                },
                onItemClick = { jobId ->
                    navigateToJobDetail(jobId)
                }
            )
        bindingDashboard.rvListPekerjaan.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = adapterListJob
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun navigateToJobDetail(jobId: Int) {
        val intent = Intent(this, DetailJobActivity::class.java).apply {
            putExtra("JOB_ID", jobId)
        }
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshAndRemove(position: Int) {
        dbHandler.deleteJob(position)
        adapterListJob.refreshList(dbHandler.getJob())
        updateActionButtons()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun navigateToAddJob() {
        val intent = Intent(this, AddJobActivity::class.java)
        addJobLauncher.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setListProduct() {
        val listProduct = ListProduct.values().toList()
        adapterListProduct = ListProductAdapter(listProduct, selectedListProduct) { listProduct ->
            selectedListProduct = listProduct
            refreshListProductAndJob()
        }

        bindingDashboard.rvListProduk.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = adapterListProduct
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setSearchFunction() {
        bindingDashboard.etCariPelanggan.doOnTextChanged { text, _, _, _ ->
            searchQuery = text.toString()
            refreshListProductAndJob()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshListProductAndJob() {
        var listJob = if (selectedListProduct == ListProduct.SEMUA_PRODUK) {
            dbHandler.getJob()
        } else {
            dbHandler.getJob().filter { it.listProduct == selectedListProduct }
        }

        if (searchQuery.isNotEmpty()) {
            listJob = listJob.filter { it.nameClient.contains(searchQuery, ignoreCase = true) }
        }

        adapterListJob.refreshList(listJob)
        adapterListProduct.updateSelectedListProduct(selectedListProduct)
    }
}
