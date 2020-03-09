package project.importers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import project.Exceptions;
import project.data.Crop;
import project.data.Farm;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test class for ProducerCSVImporter.
 */
public class ProducerCSVImporterTest {
    private Map<Integer, ArrayList<Crop>> testMap;
    private ProducerCSVImporter testImporter;

    @SuppressWarnings("MissingJavadocMethod")
    @Before
    public void setup() {
        try {
            testMap = new HashMap<>() {{ put(1, new ArrayList<Crop>()); }};
            testImporter = new ProducerCSVImporter("README.md", Crop.KG_PER_HA, "Sus", testMap);
        } catch (FileNotFoundException e) {
        }
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testCacheIsSet() {
        assertTrue(testImporter.getYields() != null);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testCacheIsSetOnEmptyMap() {
        try {
            ProducerCSVImporter importer = new ProducerCSVImporter("README.md", Crop.KG_PER_HA, "Sus", null);
            assertFalse(importer.getYields() == null);
        } catch (Exception e) {
        }
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testSetYield_NewKey() {
        Farm testFarm = new Farm("Farm", "Guelph", "Corn", 10000, Crop.KG_PER_HA, "Prod", 2020);
        testImporter.setYield(2000, testFarm);
        assertEquals(testImporter.getYields().get(2000).get(0), testFarm);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testSetYield_Present() {
        Farm testFarm = new Farm("Farm", "Guelph", "Corn", 10000, Crop.KG_PER_HA, "Prod", 2020);
        testImporter.setYield(1, testFarm);
        assertEquals(testImporter.getYields().get(1).get(0), testFarm);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetYields() {
        assertEquals(testImporter.getYields(), testMap);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testExists_FarmInCache() {
        Farm testFarm = new Farm("Farm", "Guelph", "Corn", 10000, Crop.KG_PER_HA, "Prod", 2020);
        testImporter.setYield(1, testFarm);
        assertEquals(testImporter.exists(testImporter.getYields(), testFarm), true);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testExists_FarmNotInCache() {
        Farm testFarm = new Farm("Farm", "Guelph", "Corn", 10000, Crop.KG_PER_HA, "Prod", 2020);
        assertEquals(testImporter.exists(testImporter.getYields(), testFarm), false);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testParseYield_ValidYieldWithComma() {
        try {
            assertEquals(testImporter.parseYield("2,399"), 2399.00, 0.0001);
        } catch (Exceptions.YieldInvalidException e) {
        }
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testParseYield_ValidYield() {
        try {
            assertEquals(testImporter.parseYield("2399"), 2399.00, 0.0001);
        } catch (Exceptions.YieldInvalidException e) {
        }
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test(expected = Exceptions.YieldInvalidException.class)
    public void testParseYield__InvalidYield() throws Exceptions.YieldInvalidException {
            assertEquals(testImporter.parseYield("2399aa"), 2399.00, 0.0001);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testParseYear_ValidYear() {
        try {
            assertEquals(testImporter.parseYear("2001"), 2001);
        } catch (NumberFormatException e) {
        }
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test(expected = NumberFormatException.class)
    public void testParseYear_InvalidYear() throws NumberFormatException {
        testImporter.parseYear("2001a");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetSourceUnits() {
        assertEquals(testImporter.getSourceUnits(), Crop.KG_PER_HA);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testSetSourceUnits() {
        testImporter.setSourceUnits(Crop.BU_PER_AC);
        assertEquals(testImporter.getSourceUnits(), Crop.BU_PER_AC);
    }

    @SuppressWarnings("MissingJavadocMethod")
    public static void main(String[] args) {

        Result result = JUnitCore.runClasses(ProducerCSVImporterTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());

    }
}
