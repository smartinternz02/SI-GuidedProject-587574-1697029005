package com.vit.snacksquad

data class Order(
    var userName: String? = null,
    var userEmail: String? = null,
    var phoneNo: String? = null,
    var completed: Boolean = false,
    var address: String? = null,
    var paymentMethod: String? = null,
    var restaurant: String? = null,
    var date: String? = null
)
