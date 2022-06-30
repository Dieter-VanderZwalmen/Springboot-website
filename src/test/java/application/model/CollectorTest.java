package application.model;




import application.CollectorBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CollectorTest {
    //deze class test wanneer je objecten maakt of die wel de juiste fouten opgooien


    //standaard dingen die ik gewoon ge copy paste heb en hoop dat het werkt
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

    //effectieve testen

    @Test
    public void givenValidPatient_shouldHaveNoViolations() {
        //given
        Collector johan = CollectorBuilder.aCollectorJohan().build();

        //when
        Set<ConstraintViolation<Collector>> violations = validator.validate(johan);

        //then
        assertTrue(violations.isEmpty());
    }

    @Test
    public void givenPatientWithEmptyName_shouldDetectInvalidNameError() {
        //given
        Collector johan = CollectorBuilder.anInvalidCollectorNoName().build();

        //when
        Set<ConstraintViolation<Collector>> violations = validator.validate(johan);

        //then
        assertEquals(violations.size(), 1);
        ConstraintViolation<Collector> violation = violations.iterator().next();
        assertEquals("name.missing", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals(null, violation.getInvalidValue());
    }

    //todo
    //ik weet niet of ik echt alles moet testen aangezien da super lang zou duren
    //ik vraag het eerst

}
