package com.example.servicebook.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.servicebook.models.ListProduct
import com.example.servicebook.models.PekerjaanData
import com.example.servicebook.models.Status
import java.time.LocalDate

class SQLDatabaseHandler(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "serviceBookDB"
        private const val TABLE_NAME = "pekerjaan"
        private const val DB_VERSION = 2
        private const val ID_COL = "id"
        private const val DATE_COL = "date"
        private const val CLIENT_COL = "client"
        private const val PHONE_COL = "phone_number"
        private const val PRODUCT_COL = "product"
        private const val STATUS_COL = "status"
        private const val DESCRIPTION_COL = "description"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            ("CREATE TABLE " + TABLE_NAME +
                    " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DATE_COL + " TEXT," +
                    CLIENT_COL + " TEXT," +
                    PHONE_COL + " TEXT," +
                    PRODUCT_COL + " TEXT," +
                    STATUS_COL + " TEXT," +
                    DESCRIPTION_COL + " TEXT)")
        db?.execSQL(query)
    }

    fun addJob(
        nameClient: String?,
        phoneNumber: String?,
        product: String?,
        description: String?,
        date: LocalDate,
        status: Status = Status.DITERIMA,
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(DATE_COL, date.toEpochDay())
        values.put(CLIENT_COL, nameClient)
        values.put(PHONE_COL, phoneNumber)
        values.put(PRODUCT_COL, product)
        values.put(STATUS_COL, status.name)
        values.put(DESCRIPTION_COL, description)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getJob(): ArrayList<PekerjaanData> {
        val db = this.readableDatabase
        val cursorJob = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val jobList: ArrayList<PekerjaanData> = arrayListOf()

        val idxId = cursorJob.getColumnIndexOrThrow(ID_COL)
        val idxDate = cursorJob.getColumnIndexOrThrow(DATE_COL)
        val idxClient = cursorJob.getColumnIndexOrThrow(CLIENT_COL)
        val idxPhone = cursorJob.getColumnIndexOrThrow(PHONE_COL)
        val idxProduct = cursorJob.getColumnIndexOrThrow(PRODUCT_COL)
        val idxStatus = cursorJob.getColumnIndexOrThrow(STATUS_COL)
        val idxDesc = cursorJob.getColumnIndexOrThrow(DESCRIPTION_COL)

        //Checking cursor data
        if (cursorJob.moveToFirst()) {

            do {
                val product = ListProduct.values().find { it.nameProduct == cursorJob.getString(idxProduct) }
                val status = Status.values().find { it.name == cursorJob.getString(idxStatus) }

                if (product != null && status != null) {
                    jobList.add(
                        PekerjaanData(
                            id = cursorJob.getInt(idxId),
                            date = LocalDate.ofEpochDay(cursorJob.getLong(idxDate)),
                            nameClient = cursorJob.getString(idxClient),
                            phoneNumber = cursorJob.getString(idxPhone),
                            listProduct = product,
                            status = status,
                            description = cursorJob.getString(idxDesc)
                        )
                    )
                }
            } while (cursorJob.moveToNext())
        }

        cursorJob.close()
        db.close()
        return jobList
    }

    fun getJobById(id: Int): PekerjaanData? {
        val db = this.readableDatabase
        val cursorJob =
            db.rawQuery("SELECT * FROM $TABLE_NAME WHERE id = ?", arrayOf(id.toString()))

        var jobData: PekerjaanData? = null
        //Checking cursor data
        if (cursorJob.moveToFirst()) {
            val jobId = cursorJob.getInt(0)
            val date = LocalDate.ofEpochDay(cursorJob.getLong(1))
            val nameClient = cursorJob.getString(2)
            val phoneNumber = cursorJob.getString(3)
            val product = cursorJob.getString(4)
            val status = cursorJob.getString(5)
            val description = cursorJob.getString(6)

            val setProduct = ListProduct.values().find { it.nameProduct == product }
            val onStatus = Status.values().find { it.name == status }
            if (product != null && onStatus != null) {
                jobData =
                    PekerjaanData(
                        jobId,
                        date,
                        nameClient,
                        phoneNumber,
                        setProduct,
                        onStatus,
                        description
                    )
            }
        }
        cursorJob.close()
        return jobData
    }

    fun deleteJob(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "id=?", arrayOf(id.toString()))
        db.close()
    }

    fun updateJob(
        id: Int,
        nameClient: String?,
        phoneNumber: String?,
        product: String?,
        status: Status,
        description: String?
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(CLIENT_COL, nameClient)
        values.put(PHONE_COL, phoneNumber)
        values.put(PRODUCT_COL, product)
        values.put(STATUS_COL, status.name)
        values.put(DESCRIPTION_COL, description)

        db.update(TABLE_NAME, values, "id=?", arrayOf(id.toString()))
        db.close()
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
