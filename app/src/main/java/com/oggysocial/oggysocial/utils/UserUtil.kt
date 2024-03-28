package com.oggysocial.oggysocial.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.oggysocial.oggysocial.models.User

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

        fun getFakeUsers(): List<User> {
            val users = mutableListOf<User>()
            for (i in 1..10) {
                val user = User("FirstName$i", "LastName$i", "user$i@example.com", "01/01/2000")
                user.id = "id$i"
                user.role = "user"
                user.bio = "This is a bio for user$i"
                user.blocked = false
                users.add(user)
            }
            return users
        }
    }


}
