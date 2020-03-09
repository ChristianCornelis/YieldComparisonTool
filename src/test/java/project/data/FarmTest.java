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
public class FarmTest {
    private Farm testFarm;

    @SuppressWarnings("MissingJavadocMethod")
    @Before
    public void setup() {
        testFarm = new Farm("Farm", "Guelph", "Corn", 100, Crop.KG_PER_HA, "Prod", 2020);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetName() {
        assertEquals(testFarm.getName(), "Farm");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetLocation() {
        assertEquals(testFarm.getLocation(), "Guelph");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetProducer() {
        assertEquals(testFarm.getProducer(), "Prod");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testSetLocation() {
        testFarm.setLocation("Space");
        assertEquals(testFarm.getLocation(), "Space");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testSetName() {
        testFarm.setName("Sus");
        assertEquals(testFarm.getName(), "Sus");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testSetProducer() {
        testFarm.setProducer("Prod2");
        assertEquals(testFarm.getProducer(), "Prod2");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testToString() {
        String exp = "Farm{name='Farm', location='Guelph', Crop{units=" +
                Crop.KG_PER_HA + ", type='Corn', yield=100.0, year=2020}}";
        assertEquals(testFarm.toString(), exp);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testToMap() {
        Map<String, Object> exp = new HashMap<>();
        exp.put("name", "Farm");
        exp.put("location", "Guelph");
        exp.put("producer", "Prod");
        exp.put("yield", 100.0);
        exp.put("type", "Corn");
        exp.put("units", Crop.KG_PER_HA);
        exp.put("year", 2020);
        assertEquals(testFarm.toMap(), exp);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testEquals_TwoEqualObjs() {
        Farm testDupe = new Farm("Farm", "Guelph", "Corn", 100, Crop.KG_PER_HA, "Prod", 2020);
        assertEquals(true, testFarm.equals(testDupe));
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testEquals_TwoSlightlyDifferentObjs() {
        Farm testDupe = new Farm("Farm", "Guelph", "Corn", 10000, Crop.KG_PER_HA, "Prod", 2020);
        assertEquals(false, testFarm.equals(testDupe));
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testEquals_SameObj() {
        assertEquals(true, testFarm.equals(testFarm));
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testEquals_BlankObj() {
        assertEquals(false, testFarm.equals(new Object()));
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testHashCode() {
        //essentially just check that a hash is generated
        assertTrue(testFarm.hashCode() > 0);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testNoArgConstructor() {
        Farm test = new Farm();
        assertTrue(test instanceof Farm);
    }

    @SuppressWarnings("MissingJavadocMethod")
    public static void main(String[] args) {

        Result result = JUnitCore.runClasses(FarmTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
