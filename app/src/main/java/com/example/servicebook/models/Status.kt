package com.example.servicebook.models

import androidx.annotation.ColorRes
import com.example.servicebook.R

enum class Status(val label: String, @ColorRes val color: Int){
    SEMUA_STATUS("Semua Status", color = R.color.colorButtonBackground),
    DITERIMA("Diterima", R.color.accepted),
    DIKERJAKAN("Dikerjakan", R.color.onProgress),
    SELESAI("Selesai", R.color.finish)
}

enum class ListJobState{
    NORMAL, REMOVE
}
