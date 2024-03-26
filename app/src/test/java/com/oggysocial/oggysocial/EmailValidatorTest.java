package com.oggysocial.oggysocial;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oggysocial.oggysocial.services.EmailService;

import org.junit.Test;

public class EmailValidatorTest {


    @Test
    public void emailValidator_NullEmail_AssertFalse() {
        assertFalse(EmailService.isValidEmail(""));
    }

    @Test
    public void emailValidator_CorrectEmail_AssertTrue() {
        assertTrue(EmailService.isValidEmail("abc@gmail.com"));
    }

    @Test
    public void emailValidator_IncorrectEmail_AssertFalse() {
        assertFalse(EmailService.isValidEmail("abc@gmail"));
    }

    @Test
    public void emailValidator_OnlyDomain_AssertFalse() {
        assertFalse(EmailService.isValidEmail("@gmail.com"));
    }

    @Test
    public void emailValidator_OnlyUsername_AssertFalse() {
        assertFalse(EmailService.isValidEmail("abc@"));
    }

    @Test
    public void emailValidator_SubDomain_AssertTrue() {
        assertTrue(EmailService.isValidEmail("anv@gmail.us.com"));
    }
}
