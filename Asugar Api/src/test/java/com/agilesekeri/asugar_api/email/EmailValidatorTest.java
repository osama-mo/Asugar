package com.agilesekeri.asugar_api.email;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmailValidator.class})
@ExtendWith(SpringExtension.class)
class EmailValidatorTest {
    @Autowired
    private EmailValidator emailValidator;

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testTest2() {
        assertFalse(emailValidator.test("java.lang.String"));
    }

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testTest3() {
        assertFalse(emailValidator.test("@java.lang.String"));
    }

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testTest4() {
        assertFalse(emailValidator.test("foo@foo"));
    }

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testTest5() {
        assertTrue(emailValidator.test("foo@."));
    }
}

