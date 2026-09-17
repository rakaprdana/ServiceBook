package com.example.servicebook.models

import java.time.LocalDate

data class PekerjaanData(
    var id: Int,
    var date: LocalDate,
    var nameClient: String,
    var phoneNumber: String,
    var listProduct: ListProduct?,
    var status: Status = Status.DITERIMA,
    var description: String
)