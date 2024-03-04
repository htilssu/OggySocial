package com.oggysocial.oggysocial.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.oggysocial.oggysocial.models.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Mock
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Mock
    private DocumentSnapshot documentSnapshot = mock(DocumentSnapshot.class);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(firebaseAuth.getUid()).thenReturn("testUid");
        when(firebaseFirestore.collection("users").document("testUid").get().getResult()).thenReturn(documentSnapshot);
        when(documentSnapshot.toObject(User.class)).thenReturn(new User());
    }

    @Test
    public void getUserByIdReturnsUserWhenUserExists() {
        UserService.getUserById("testUid");
        verify(firebaseFirestore.collection("users").document("testUid").get(), times(1)).addOnSuccessListener(any());
    }

    @Test
    public void getUserByIdLogsErrorWhenExceptionOccurs() {
        when(firebaseFirestore.collection("users").document("testUid").get()).thenThrow(new RuntimeException());
        UserService.getUserById("testUid");
        verify(firebaseFirestore.collection("users").document("testUid").get(), times(1)).addOnFailureListener(any());
    }

    @Test
    public void getUserReturnsUserWhenUserExists() {
        UserService.getUser();
        verify(firebaseFirestore.collection("users").document("testUid").get(), times(1)).addOnSuccessListener(any());
    }

    @Test
    public void getUserLogsErrorWhenExceptionOccurs() {
        when(firebaseFirestore.collection("users").document("testUid").get()).thenThrow(new RuntimeException());
        UserService.getUser();
        verify(firebaseFirestore.collection("users").document("testUid").get(), times(1)).addOnFailureListener(any());
    }

    @Test
    public void saveUserSavesUserToDatabase() {
        User user = new User();
        UserService.saveUser(user);
        verify(firebaseFirestore.collection("users").document("testUid"), times(1)).set(user);
    }
}