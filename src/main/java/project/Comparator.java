package project;

import java.util.ArrayList;
import java.util.Set;

/**
 * Interface for comparing data. Could be yield, area, or production
 */
public interface Comparator {
    /**
     * Calculates the difference between the first value and the second value.
     * @param value1 First value to compare.
     * @param value2 Second value to compare.
     * @return the difference.
     */
    double difference(double value1, double value2);

    /**
     * Calculates the intersection of two maps' keysets.
     * @param set1 first map
     * @param set2 second map
     * @return ArrayList representing the intersection.
     */
    ArrayList<Integer> keyIntersection(Set<Integer> set1, Set<Integer> set2);
}
