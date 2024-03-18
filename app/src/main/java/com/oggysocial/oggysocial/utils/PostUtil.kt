package com.oggysocial.oggysocial.utils

import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.ZoneOffset
import java.util.Date

class PostUtil {
    companion object {
        fun getPostCount(): Int {
            return 10
        }

        fun getPostCountForDayOfMonth(month: Month, listener: OnPostCountListener? = null) {
            getPostCountForDayOfMonth(month.value, listener)
        }

        fun getPostCountForDayOfMonth(month: Int, listener: OnPostCountListener? = null) {
            val postCountList = MutableList(31) { 0 }
            val db = FirebaseFirestore.getInstance()

            //Get day of month
            val year = LocalDate.now().year
            val date = LocalDate.of(year, month, 1)
            val daysInMonth = date.lengthOfMonth()
            for (i in 1..daysInMonth) {
                val startOfDay =
                    Date.from(LocalDate.of(year, month, i).atStartOfDay().toInstant(ZoneOffset.UTC))
                val endOfDay = Date.from(
                    LocalDate.of(year, month, i).atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)
                )
                val query = db.collection("posts").whereGreaterThanOrEqualTo("date", startOfDay)
                    .whereLessThanOrEqualTo("date", endOfDay)
                query.addSnapshotListener { value, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        postCountList[i - 1] = value.size()
                        listener?.onPostCountReceived(postCountList)
                    }
                }

            }
        }


    }

    interface OnPostCountListener {
        fun onPostCountReceived(postCountList: List<Int>)
    }
}