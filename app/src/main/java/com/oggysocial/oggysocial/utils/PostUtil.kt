package com.oggysocial.oggysocial.utils

import android.os.Build
import com.google.firebase.firestore.Filter.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.oggysocial.oggysocial.models.Post
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.Date

class PostUtil {
    companion object {

        fun getPostCountForDayOfMonth(
            month: Month,
            listener: (List<Int>) -> Unit,
            cancel: (ListenerRegistration) -> Unit
        ) {
            getPostCountForDayOfMonth(month.value, listener, cancel)
        }

        fun getPostCountForDayOfMonth(
            month: Int,
            listener: (List<Int>) -> Unit,
            cancel: (ListenerRegistration) -> Unit
        ) {
            val postCountList = MutableList(31) { 0 }
            val db = FirebaseFirestore.getInstance()

            //Get day of month
            val year = LocalDate.now().year
            val dayOfMonth = LocalDate.of(year, month, 1).lengthOfMonth()
            val startMonth = Date.from(
                LocalDate.of(year, month, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()
            )

            val endMonth =
                Date.from(
                    LocalDate.of(year, month, dayOfMonth).atStartOfDay()
                        .atZone(ZoneId.systemDefault()).toInstant()
                )

            val event = db.collection("posts")
                .where(
                    and(
                        greaterThanOrEqualTo("date", startMonth),
                        lessThanOrEqualTo("date", endMonth)
                    )
                )
                .addSnapshotListener { snapshot, _ ->

                    val listPost = snapshot?.toObjects(Post::class.java)
                    listPost?.forEach { post ->
                        val instant = post.date.toInstant()
                        var day = 0
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            day = LocalDate.ofInstant(instant, ZoneId.systemDefault()).dayOfMonth
                        }
                        postCountList[day - 1]++
                    }
                    listener(postCountList)
                }

            cancel(event)

        }

        fun getTotalPostCount(listener: (Any) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("posts")

            query.addSnapshotListener { snapshot, _ ->

                if (snapshot != null) {
                    val postCount = snapshot.size()
                    listener(postCount)
                }
            }
        }

    }

    interface OnPostCountListener {
        fun onPostCountReceived(postCountList: List<Int>)
    }

    interface OnPostListener<T> {
        fun onPostReceived(value: T)
    }
}