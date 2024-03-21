package com.oggysocial.oggysocial.utils

import com.google.firebase.firestore.FirebaseFirestore

class UserUtil {
    companion object {
        fun getTotalUserCount(listener: (Int) -> Unit) {
            val db = FirebaseFirestore.getInstance()
            val query = db.collection("users")
            query.addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val userCount = snapshot.size()
                    listener(userCount)
                }
            }

        }
    }

}
