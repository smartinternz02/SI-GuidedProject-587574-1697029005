package com.vit.snacksquad

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class OrderHelper {
    private val db = Firebase.firestore

    fun placeOrder(
        userName: String?,
        userEmail: String?,
        phoneNo: String,
        address: String,
        paymentMethod: String,
        restaurant: String
    ) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val monthName = calendar.getDisplayName(month, Calendar.SHORT, Locale.US)
        val date = "$day $monthName"
        val order = Order(
            userName,
            userEmail,
            phoneNo,
            false,
            address,
            paymentMethod,
            restaurant,
            date
        )

        db.collection("Orders").document(userName!!).set(order)
            .addOnSuccessListener { Log.d("PlaceOrder", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("PlaceOrder", "Error writing document", e) }
    }
}