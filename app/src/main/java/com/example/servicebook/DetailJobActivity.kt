package com.example.servicebook

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.servicebook.database.SQLDatabaseHandler
import com.example.servicebook.databinding.ActivityDetailJobBinding

class DetailJobActivity : AppCompatActivity() {
    private lateinit var bindingDetail: ActivityDetailJobBinding
    private val dbHandler: SQLDatabaseHandler = SQLDatabaseHandler(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingDetail = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(bindingDetail.root)

        val jobId = intent.getIntExtra("JOB_ID", -1)
        if (jobId != -1) {
            val job = dbHandler.getJobById(jobId)
            if (job != null) {
                bindingDetail.tvNamaPelanggan.text = job.nameClient
                bindingDetail.tvNomorHp.text = job.phoneNumber
                bindingDetail.tvJenisBarang.text = job.listProduct?.nameProduct ?: ""
                bindingDetail.tvStatus.text = job.status.name
                bindingDetail.layoutStatus.setCardBackgroundColor(getColor(job.status.color))
                bindingDetail.tvDeskripsi.text = job.description
            }
        }

        bindingDetail.ivKembali.setOnClickListener {
            finish()
        }
    }
}
