package project.importers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import project.data.Crop;
import project.data.Farm;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class StatsCanCSVImporterTest {
    private Map<Integer, ArrayList<Crop>> testMap;
    private StatsCanCSVImporter testImporter;

    @SuppressWarnings("MissingJavadocMethod")
    @Before
    public void setup() {
        try {
            testMap = new HashMap<>() {{ put(1, new ArrayList<Crop>()); }};
            testImporter = new StatsCanCSVImporter("README.md", Crop.KG_PER_HA,  testMap);
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
            StatsCanCSVImporter importer = new StatsCanCSVImporter("README.md", Crop.KG_PER_HA, null);
            assertFalse(importer.getYields() == null);
        } catch (Exception e) {
        }
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testSetYield_NewKey() {
        Crop testCrop = new Crop( "Corn", 10000, Crop.KG_PER_HA, 2020);
        testImporter.setYield(2000, testCrop);
        assertEquals(testImporter.getYields().get(2000).get(0), testCrop);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testSetYield_Present() {
        Crop testCrop = new Crop( "Corn", 10000, Crop.KG_PER_HA, 2020);
        testImporter.setYield(1, testCrop);
        assertEquals(testImporter.getYields().get(1).get(0), testCrop);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetYields() {
        assertEquals(testImporter.getYields(), testMap);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testExists_FarmInCache() {
        Crop testCrop = new Crop( "Corn", 10000, Crop.KG_PER_HA, 2020);
        testImporter.setYield(1, testCrop);
        assertEquals(testImporter.exists(testImporter.getYields(), testCrop), true);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testExists_FarmNotInCache() {
        Crop testCrop = new Crop( "Corn", 10000, Crop.KG_PER_HA, 2020);
        assertEquals(testImporter.exists(testImporter.getYields(), testCrop), false);
    }

    @SuppressWarnings("MissingJavadocMethod")
    public static void main(String[] args) {

        Result result = JUnitCore.runClasses(StatsCanCSVImporterTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());

    }
}
