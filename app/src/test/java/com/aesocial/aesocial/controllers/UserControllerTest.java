package com.aesocial.aesocial.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserControllerTest {

    @Mock
    private FirebaseAuth firebaseAuth;

    @Mock
    private FirebaseDatabase firebaseDatabase;

    @Mock
    private Task<AuthResult> authResultTask;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerUserSuccessfully() {
        when(firebaseAuth.createUserWithEmailAndPassword("test@test.com", "password")).thenReturn(authResultTask);
        when(authResultTask.isSuccessful()).thenReturn(true);
        assertTrue(UserController.registerUser("John", "Doe", "test@test.com", "password", "01/01/2000"));
    }

    @Test
    public void registerUserFailed() {
        when(firebaseAuth.createUserWithEmailAndPassword("test@test.com", "password")).thenReturn(authResultTask);
        when(authResultTask.isSuccessful()).thenReturn(false);
        assertFalse(UserController.registerUser("John", "Doe", "test@test.com", "password", "01/01/2000"));
    }

    @Test
    public void logOutSuccessfully() {
        assertTrue(UserController.logOut());
    }

    @Test
    public void isLoginTrue() {
        when(firebaseAuth.getCurrentUser()).thenReturn(null);
        assertFalse(UserController.isLogin());
    }

    @Test
    public void isLoginFalse() {
        when(firebaseAuth.getCurrentUser()).thenReturn(null);
        assertFalse(UserController.isLogin());
    }
}