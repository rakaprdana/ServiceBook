package com.example.servicebook.models

data class PekerjaanData(
    var id: Int,
    var nameClient: String,
    var phoneNumber: String,
    var listProduct: ListProduct?,
    var status: Status = Status.DITERIMA,
    var description: String
)