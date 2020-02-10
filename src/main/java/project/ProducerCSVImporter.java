package project;

import java.io.FileNotFoundException;

public class ProducerCSVImporter extends CSVImporter {

    /**
     * Constructor.
     * @param filename the file to parse.
     * @param su the source units for yield.
     * @throws FileNotFoundException if the filename cannot be found.
     */
    public ProducerCSVImporter(String filename, int su) throws FileNotFoundException {
        super(filename, su);
    }

//    public Farm getFarm(String) {
//
//    }

    /**
     * Parses the CSV containing producer yield data.
     */
    public void parse() {
        String[] tokens = parseLine();  //skip headers
        while ((tokens = parseLine()) != null) {
            String farmName = tokens[0];
            String location = tokens[1];
            String cropName = tokens[2];
            int year = parseYear(tokens[3]);
            String yieldString = tokens[4];
            double yield = 0;
            try {
                yield = parseYield(yieldString);
//                yield = convertYield(yield, cropName);
            } catch (Exceptions.YieldInvalidException e) {
                System.err.println(e.getMessage());
            }
            System.out.println("Adding farm " + farmName + " crop " + cropName + " year " + year + " yield " + yield);
            Farm toPut = new Farm(farmName, location, cropName, yield, super.getSourceUnits());
            super.setYield(year, toPut);
        }
    }

    /**
     * Driver program.
     * @param args args for prog
     */
    public static void main(String[] args) {
        String fileName = args[0];
        int sourceUnits = Integer.parseInt(args[1]);
        try {
            ProducerCSVImporter pci = new ProducerCSVImporter(fileName, sourceUnits);
            pci.parse();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
