package project.comparators;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import project.Exceptions;
import project.data.Crop;
import project.data.Farm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for YieldComparator class.
 */
public class YieldComparatorTest {

    private YieldComparator testComparator;
    private Map<Integer, ArrayList<Crop>> producerYields;
    private Map<Integer, ArrayList<Crop>> statsCanYields;

    @SuppressWarnings("MissingJavadocMethod")
    @Before
    public void setup() {
        //setup producer yields cache
        producerYields = new HashMap<>();
        Crop pTestCrop1 = new Crop("Corn", 100, Crop.KG_PER_HA, 2020);
        Crop pTestCrop2 = new Crop("Soybeans", 1000, Crop.KG_PER_HA, 2020);
        Crop pTestCrop3 = new Crop("Corn", 101, Crop.KG_PER_HA, 2019);

        ArrayList<Crop> pList1 = new ArrayList<Crop>() {{
            add(new Farm("Farm1", "Guelph", "Corn", 100, Crop.KG_PER_HA, "Conway Twitty", 2020));
            add(new Farm("Farm2", "Guelph", "Soybeans", 1000, Crop.KG_PER_HA, "Conway Twitty", 2020));
        }};

        ArrayList<Crop> pList2 = new ArrayList<Crop>() {{
            add(new Farm("Farm1", "Guelph", "Corn", 101, Crop.KG_PER_HA, "Conway Twitty", 2019));
        }};

        producerYields.put(2020, pList1);
        producerYields.put(2019, pList2);


        //setup statscan yields cache
        statsCanYields = new HashMap<>();

        ArrayList<Crop> sList1 = new ArrayList<Crop>() {{
            add(new Crop("Corn", 200, Crop.LBS_PER_AC, 2020));
            add(new Crop("Adzuki beans", 1050, Crop.KG_PER_HA, 2020));
        }};

        ArrayList<Crop> sList2 = new ArrayList<Crop>() {{
            new Crop("Corn", 11, Crop.KG_PER_HA, 2015);
        }};

        statsCanYields.put(2020, sList1);
        statsCanYields.put(2019, sList2);
        testComparator = new YieldComparator(Crop.LBS_PER_AC, producerYields, statsCanYields);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetDifference() {
        assertEquals(testComparator.getDifference(10, 8.5), 1.5, 0.0001);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testKeyIntersection_KeysInBoth() {
        ArrayList<Integer> intersection = testComparator.keyIntersection(producerYields.keySet(), statsCanYields.keySet());
        ArrayList<Integer> exp = new ArrayList<Integer>() {{
            add(2019);
            add(2020);
        }};
        assertEquals(exp, intersection);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testKeyIntersection_KeysNotInProducers() {
        producerYields = new HashMap<>(); //reset the cache
        ArrayList<Integer> intersection = testComparator.keyIntersection(producerYields.keySet(), statsCanYields.keySet());
        ArrayList<Integer> exp = new ArrayList<Integer>();
        assertEquals(exp, intersection);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testKeyIntersection_KeysNotInStatsCan() {
        statsCanYields = new HashMap<>(); //reset the cache
        ArrayList<Integer> intersection = testComparator.keyIntersection(producerYields.keySet(), statsCanYields.keySet());
        ArrayList<Integer> exp = new ArrayList<Integer>();
        assertEquals(exp, intersection);
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testCompareCropByYear_CropInBothCaches() {
        try {
            double actual = testComparator.compareCropsByYear("Corn", 2020);
            assertEquals(-110.7821, actual, 0.001);
        } catch (Exception e) {
        }
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test(expected = Exceptions.InvalidComparatorParamsException.class)
    public void testCompareCropByYear_CropInProducerCache() throws Exceptions.InvalidComparatorParamsException {
        try {
            double actual = testComparator.compareCropsByYear("Corn", 2019);
        } catch (Exceptions.BushelsConversionKeyNotFoundException e) {

        }
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test(expected = Exception.class)
    public void testCompareCropByYear_CropInStatsCanCacheOnly_NoMapKeyForYear()
            throws Exceptions.InvalidComparatorParamsException, Exceptions.BushelsConversionKeyNotFoundException {
        try {
            double actual = testComparator.compareCropsByYear("Corn", 2015);
        } catch (Exception e) {
            //ensure correct exception was thrown
            String msg = "Desired year not in both caches!";
            assertEquals(msg, e.getMessage());
            throw e;
        }
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test(expected = Exception.class)
    public void testCompareCropByYear_CropInStatsCanCacheOnly_MapKeyForYear()
            throws Exceptions.InvalidComparatorParamsException, Exceptions.BushelsConversionKeyNotFoundException {
        try {
            double actual = testComparator.compareCropsByYear("Adzuki beans", 2020);
        } catch (Exception e) {
            //ensure correct exception was thrown
            String msg = "The Producer yield map does not contain the crop Adzuki beans!";
            assertEquals(msg, e.getMessage());
            throw e;
        }
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test(expected = Exception.class)
    public void testCompareCropByYear_CropInProducersCacheOnly()
            throws Exceptions.InvalidComparatorParamsException, Exceptions.BushelsConversionKeyNotFoundException {
        try {
            double actual = testComparator.compareCropsByYear("Corn", 2019);
        } catch (Exception e) {
            //ensure correct exception was thrown
            String msg = "The Statistics Canada yield map does not contain the crop Corn!";
            assertEquals(msg, e.getMessage());
            throw e;
        }
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testFormatUnitsString_kgPerHa() {
        assertEquals(testComparator.formatUnitsString(Crop.KG_PER_HA), "kg/ha");
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testFormatUnitsString_lbsPerAc() {
        assertEquals(testComparator.formatUnitsString(Crop.LBS_PER_AC), "lbs/ac");
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testFormatUnitsString_buPerAc() {
        assertEquals(testComparator.formatUnitsString(Crop.BU_PER_AC), "bu/ac");
    }

    @SuppressWarnings({"MissingJavadocMethod", "MethodName"})
    @Test
    public void testFormatUnitsString_InvalidType() {
        assertEquals(testComparator.formatUnitsString(10), "");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testFormatComparison() {
        String actual = testComparator.formatComparison(100, "Corn", 3020, "kg/ha");
        assertEquals(actual, "The difference between your Corn yield and national avg yields in 3020 was:\n100.00 kg/ha");
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetConverter() {
        testComparator.setConverter(null);
        assertEquals(testComparator.getConverter(), null);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetTargetUnits() {
        assertEquals(testComparator.getTargetUnits(), Crop.LBS_PER_AC);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testGetStatsCanYields() {
        assertEquals(testComparator.getStatsCanYields(), statsCanYields);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void  testSetTargetUnits() {
        testComparator.setTargetUnits(Crop.BU_PER_AC);
        assertEquals(testComparator.getTargetUnits(), Crop.BU_PER_AC);
    }

    @SuppressWarnings("MissingJavadocMethod")
    @Test
    public void testSetStatsCanYields() {
        testComparator.setStatsCanYields(null);
        assertEquals(testComparator.getStatsCanYields(), null);
    }


    @SuppressWarnings("MissingJavadocMethod")
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(YieldComparatorTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());

    }
}
