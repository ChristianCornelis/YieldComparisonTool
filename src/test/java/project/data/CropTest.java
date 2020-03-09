package project.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("MissingJavadocMethod")
public class CropTest {
    private Crop testCrop;

    @SuppressWarnings("MissingJavadocMethod")
    @Before
    public void setup() {
        testCrop = new Crop("Corn", 100, Crop.KG_PER_HA, 2020);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Before
    public void testGetYield() {
        assertEquals(testCrop.getYield(), 100, 0);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetUnits() {
        assertEquals(testCrop.getUnits(), Crop.KG_PER_HA);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetType() {
        assertEquals(testCrop.getType(), "Corn");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetYear() {
        assertEquals(testCrop.getYear(), 2020);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testSetYield() {
        testCrop.setYield(1000);
        assertEquals(testCrop.getYield(), 1000, 0);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testSetType() {
        testCrop.setType("BEANS");
        assertEquals(testCrop.getType(), "BEANS");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testSetYear() {
        testCrop.setYear(3020);
        assertEquals(testCrop.getYear(), 3020);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testSetUnits() {
        testCrop.setUnits(Crop.BU_PER_AC);
        assertEquals(testCrop.getUnits(), Crop.BU_PER_AC);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testToString() {
        String exp = "Crop{units=" + Crop.KG_PER_HA + ", type='Corn', yield=100.0, year=2020}";
        assertEquals(exp, testCrop.toString());
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testToMap() {
        Map<String, Object> exp = new HashMap<>();
        exp.put("yield", 100.0);
        exp.put("type", "Corn");
        exp.put("units", Crop.KG_PER_HA);
        exp.put("year", 2020);
        assertEquals(testCrop.toMap(), exp);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testEquals_TwoEqualObjs() {
        Crop testDupe = new Crop("Corn", 100, Crop.KG_PER_HA, 2020);
        assertEquals(true, testCrop.equals(testDupe));
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testEquals_TwoSlightlyDifferentObjs() {
        Crop testDupe = new Crop("Corn", 1000, Crop.KG_PER_HA, 2020);
        assertEquals(false, testCrop.equals(testDupe));
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testEquals_SameObj() {
        assertEquals(true, testCrop.equals(testCrop));
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testEquals_BlankObj() {
        assertEquals(false, testCrop.equals(new Object()));
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testHashCode() {
        //essentially just check that a hash is generated
        assertTrue(testCrop.hashCode() > 0);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testNoArgConstructor() {
        Crop test = new Crop();
        assertTrue(test instanceof Crop);
    }

    @SuppressWarnings("MissingJavadocMethod")
    public static void main(String[] args) {

        Result result = JUnitCore.runClasses(CropTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());

    }
}
