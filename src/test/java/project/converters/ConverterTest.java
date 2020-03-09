package project.converters;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import project.Exceptions;

@SuppressWarnings("MissingJavadocMethod")
/**
 * Class to test the converter class used to convert between emtric and imperial units.
 */
public class ConverterTest {

    private Converter testConverter;

    @SuppressWarnings("MissingJavadocMethod")
    @Before
    public void setup() {
        testConverter = new Converter();
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testAcresToHectares() {
        double acres = 2.4710538147;
        double ha = testConverter.acresToHectares(acres);
        assertEquals(ha, 1, 0.001);  //allow precision to be slightly off
    }
    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testHectaresToAcres() {
        double ha = 0.4046856422;
        double ac = testConverter.hectaresToAcres(ha);
        assertEquals(ac, 1, 0.001);  //allow precision to be slightly off
    }
    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testLbsToKilos() {
        double lbs = 2.20462;
        double kgs = testConverter.lbsToKilos(lbs);
        assertEquals(kgs, 1, 0.001);
    }
    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testKilosToLbs() {
        double kgs = 0.453592;
        double lbs = testConverter.kilosToLbs(kgs);
        assertEquals(lbs, 1, 0.001);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testKgPerHaToLbsPerAc() {
        double kgPerHa = 1.12085;
        double lbsPerAc = testConverter.kgPerHaToLbsPerAc(kgPerHa);
        assertEquals(lbsPerAc, 1, 0.001);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testLbsPerAcToKgPerHa() {
        double lbsPerAc = 0.892179;
        double kgPerHa = testConverter.lbsPerAcToKgPerHa(lbsPerAc);
        assertEquals(kgPerHa, 1, 0.001);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void TestBuPerAcToLbsPerAc_ValidBushelConversionKey() throws Exceptions.BushelsConversionKeyNotFoundException {
        double lbsPerAc = testConverter.buPerAcToLbsPerAc(1, "Corn");
        assertEquals(lbsPerAc, 56, 0.001);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test(expected = Exceptions.BushelsConversionKeyNotFoundException.class)
    public void TestBuPerAcToLbsPerAc_InvalidBushelConversionKey() throws Exceptions.BushelsConversionKeyNotFoundException {
        double lbsPerAc = testConverter.buPerAcToLbsPerAc(1, "Fruit Salad");
    }

    @SuppressWarnings("MissingJavadocMethod")
    public static void main(String[] args) {

        Result result = JUnitCore.runClasses(ConverterTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());

    }

}
