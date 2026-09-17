package com.example.servicebook.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.servicebook.models.ListProduct
import com.example.servicebook.models.PekerjaanData
import com.example.servicebook.models.Status

class SQLDatabaseHandler(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "serviceBookDB"
        private const val TABLE_NAME = "pekerjaan"
        private const val DB_VERSION = 1
        private const val ID_COL = "id"
        private const val CLIENT_COL = "client"
        private const val PHONE_COL = "phone number"
        private const val PRODUCT_COL = "product"
        private const val STATUS_COL = "status"
        private const val DESCRIPTION_COL = "description"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            ("CREATE TABLE " + TABLE_NAME +
                    " (" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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
//        onStatus: String?,
        description: String?
    ) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(CLIENT_COL, nameClient)
        values.put(PHONE_COL, phoneNumber)
        values.put(PRODUCT_COL, product)
//        values.put(STATUS_COL, onStatus)
        values.put(DESCRIPTION_COL, description)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getJob(): ArrayList<PekerjaanData> {
        val db = this.readableDatabase
        val cursorJob = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val jobList: ArrayList<PekerjaanData> = arrayListOf()

        //Checking cursor data
        if (cursorJob.moveToFirst()) {

            do {
                val product = ListProduct.values().find { it.nameProduct == cursorJob.getString(3) }
                val status = Status.values().find { it.name == cursorJob.getString(4) }

                if (product != null && status != null) {
                    jobList.add(
                        PekerjaanData(
                            cursorJob.getInt(0),
                            cursorJob.getString(1),
                            cursorJob.getString(2),
                            product,
                            status,
                            cursorJob.getString(5)
                        )
                    )
                }
            } while (cursorJob.moveToNext())
        }

        cursorJob.close()
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
            val nameClient = cursorJob.getString(1)
            val phoneNumber = cursorJob.getString(2)
            val product = cursorJob.getString(3)
            val status = cursorJob.getString(4)
            val description = cursorJob.getString(5)

            val setProduct = ListProduct.values().find { it.nameProduct == product }
            val onStatus = Status.values().find { it.name == status }
            if (product != null &&onStatus != null) {
                jobData = PekerjaanData(jobId, nameClient, phoneNumber, setProduct, onStatus, description)
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

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVerstion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}