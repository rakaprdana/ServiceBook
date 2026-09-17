package com.example.servicebook

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicebook.database.SQLDatabaseHandler
import com.example.servicebook.databinding.ActivityAddBinding
import com.example.servicebook.models.ListProduct
import java.time.LocalDate

class AddJobActivity : AppCompatActivity() {
    private lateinit var bindingAddJobActivity: ActivityAddBinding
    private val dbHandler: SQLDatabaseHandler = SQLDatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingAddJobActivity = ActivityAddBinding.inflate(layoutInflater)
        setContentView(bindingAddJobActivity.root)
        setSpinnerListProduct()

        bindingAddJobActivity.btnSimpan.setOnClickListener {
            if (bindingAddJobActivity.etNamaPelanggan.text.isNullOrEmpty() ||
                bindingAddJobActivity.etNomorHp.text.isNullOrEmpty() ||
                bindingAddJobActivity.spinnerJenisBarang.selectedItem
                    .toString()
                    .isEmpty() ||
                bindingAddJobActivity.etDeskripsi.text.isNullOrEmpty()
            ) {
                return@setOnClickListener
            }
            dbHandler.addJob(
                bindingAddJobActivity.etNamaPelanggan.text.toString(),
                bindingAddJobActivity.etNomorHp.text.toString(),
                bindingAddJobActivity.spinnerJenisBarang.selectedItem.toString(),
                bindingAddJobActivity.etDeskripsi.text.toString(),
                LocalDate.now(),
                )
            setResult(200, Intent())
            finish()
        }

        bindingAddJobActivity.ivKembali.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setSpinnerListProduct() {
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                getListProduct()
            )
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        bindingAddJobActivity.spinnerJenisBarang.adapter = adapter
    }

    private fun getListProduct(): List<String> {
        return ListProduct.values().map {
            if (it == ListProduct.SEMUA_PRODUK) {
                ""
            } else {
                it.nameProduct
            }
        }
    }
}