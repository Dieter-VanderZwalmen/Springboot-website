package application.model;

import application.CoinBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CoinTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Test
    public void givenValidCoin_shouldHaveNoViolations() {
        //given
        Coin coin = CoinBuilder.aValidCoin().build();

        //when
        Set<ConstraintViolation<Coin>> violations = validator.validate(coin);

        //then
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenCoinWithEmptyName_shouldThrowNameMissingError() {
        //given
        Coin coin = CoinBuilder.anCoinWithEmptyName().build();

        //when
        Set<ConstraintViolation<Coin>> violations = validator.validate(coin);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<Coin> violation = violations.iterator().next();
        assertEquals("munt.name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());

    }

    @Test
    public void givenCoinWithNegativeValue_shouldThrowNegativeValueError() {
        //given
        Coin coin = CoinBuilder.aCoinWithNegativeValue().build();

        //when
        Set<ConstraintViolation<Coin>> violations = validator.validate(coin);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<Coin> violation = violations.iterator().next();
        assertEquals("must be greater than or equal to 0", violation.getMessage());
        assertEquals("value", violation.getPropertyPath().toString());
        assertEquals(-2.0, violation.getInvalidValue());

    }
}
