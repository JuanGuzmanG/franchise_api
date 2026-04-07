package com.jjgg.franchise_api.Application.dto.command;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddProductRequestDtoValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void shouldFailWhenNameIsBlankAndStockIsNegative() {
        AddProductRequestDto dto = new AddProductRequestDto("", -1);

        Set<?> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassWhenRequestIsValid() {
        AddProductRequestDto dto = new AddProductRequestDto("Producto A", 10);

        Set<?> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }
}

