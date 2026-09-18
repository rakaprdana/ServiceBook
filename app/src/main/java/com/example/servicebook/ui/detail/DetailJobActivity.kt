package com.example.servicebook.ui.detail

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.servicebook.data.local.SQLDatabaseHandler
import com.example.servicebook.databinding.ActivityDetailJobBinding
import com.example.servicebook.models.ListProduct
import com.example.servicebook.models.PekerjaanData
import com.example.servicebook.models.Status

class DetailJobActivity : AppCompatActivity() {
    private lateinit var bindingDetail: ActivityDetailJobBinding
    private val dbHandler: SQLDatabaseHandler = SQLDatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingDetail = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(bindingDetail.root)

        val jobId = intent.getIntExtra("JOB_ID", -1)
        if (jobId != -1) {
            val job = dbHandler.getJobById(jobId)
            if (job != null) {
                populateJobData(job)
            }
        }

        bindingDetail.ivKembali.setOnClickListener {
            finish()
        }
    }

    private fun populateJobData(job: PekerjaanData) {
        bindingDetail.etNamaPelanggan.setText(job.nameClient)
        bindingDetail.etNomorHp.setText(job.phoneNumber)
        bindingDetail.etDeskripsi.setText(job.description)

        val products = ListProduct.values().filter { it != ListProduct.SEMUA_PRODUK }
        val productAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            products.map { it.nameProduct }
        )
        productAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        bindingDetail.spinnerJenisBarang.adapter = productAdapter

        val productIndex = products.indexOf(job.listProduct)
        if (productIndex != -1) {
            bindingDetail.spinnerJenisBarang.setSelection(productIndex)
        }

        val statuses = Status.values().filter { it != Status.SEMUA_STATUS }
        val statusAdapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            statuses.map { it.label }
        )
        statusAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        bindingDetail.spinnerStatus.adapter = statusAdapter

        val statusIndex = statuses.indexOf(job.status)
        if (statusIndex != -1) {
            bindingDetail.spinnerStatus.setSelection(statusIndex)
        }

        bindingDetail.btnSimpan.setOnClickListener {
            saveJobData(job.id, products, statuses)
        }
    }

    private fun saveJobData(jobId: Int, products: List<ListProduct>, statuses: List<Status>) {
        val name = bindingDetail.etNamaPelanggan.text.toString().trim()
        val phone = bindingDetail.etNomorHp.text.toString().trim()
        val description = bindingDetail.etDeskripsi.text.toString().trim()

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedProduct = products[bindingDetail.spinnerJenisBarang.selectedItemPosition]
        val selectedStatus = statuses[bindingDetail.spinnerStatus.selectedItemPosition]

        dbHandler.updateJob(
            id = jobId,
            nameClient = name,
            phoneNumber = phone,
            product = selectedProduct.nameProduct,
            status = selectedStatus,
            description = description
        )

        Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
        finish()
    }
}
