package project.importers;

import project.Exceptions;

public interface Importer {

    /**
     * Converts year to an int.
     * @param year The year as a string.
     * @return The year as an int.
     */
    int parseYear(String year);

    /**
     * Parses a yield string. Removes any commas from yield data.
     * @param yield the yield to be parsed.
     * @return the yield as a double
     * @throws Exceptions.YieldInvalidException thrown if the yield is not valid.
     */
    double parseYield(String yield) throws Exceptions.YieldInvalidException;

    /**
     * Parses the file being imported.
     */
    void parse();
}
