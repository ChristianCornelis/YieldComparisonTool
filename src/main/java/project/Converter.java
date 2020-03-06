package project;

import java.util.Map;
import project.Exceptions.BushelsConversionKeyNotFoundException;
import static java.util.Map.entry;

public class Converter {
    private static final double KGS_PER_HA_TO_LBS_PER_AC_FACTOR = 0.892179;
    private static final double LBS_PER_AC_TO_KG_PER_HA_FACTOR = 1.12085;
    private static final double KGS_TO_LBS_FACTOR = 2.20462;
    private static final double LBS_TO_KGS_FACTOR = 0.453592;
    private static final double HA_TO_AC_FACTOR = 2.47105;
    private static final double AC_TO_HA_FACTOR = 0.404686;
    private static final Map<String, Integer> BU_TO_LBS = Map.ofEntries(
            entry("Camelina", 50),
            entry("Corn", 56),
            entry("Beans", 60),
            entry("Alfalfa", 60),
            entry("Rye", 56),
            entry("Flaxseed", 56),
            entry("Oats", 32),
            entry("Canola", 50),
            entry("Wheat", 60),
            entry("Sugar beets", 52),
            entry("Millet", 50),
            entry("Peas", 60),
            entry("Mustard", 50),
            entry("Lentils", 60),
            entry("Coriander", 28),
            entry("Sunflower", 30),
            entry("Triticale", 52),
            entry("Buckwheat", 50),
            entry("Canary", 50),
            entry("Hemp", 44),
            entry("Safflower", 38),
            entry("Barley", 48)
    );

    /**
     * Converts a yield from the source units to the desired target units.
     * @param yield The yield to convert
     * @param cropType the crop type
     * @param sourceUnits the units the source data is in
     * @param targetUnits the targetted units to convert to
     * @return the yield in the desired units
     * @throws BushelsConversionKeyNotFoundException thrown if the crop type is not present in the bushels conversion map.
     */
    public double convertYield(Double yield, String cropType, int sourceUnits, int targetUnits)
            throws BushelsConversionKeyNotFoundException {
        //convert yield according to what source and target units are
        if (targetUnits != sourceUnits) {
            switch (sourceUnits) {
                case Crop.KG_PER_HA:
                    if (targetUnits == Crop.LBS_PER_AC) {
                        return kgPerHaToLbsPerAc(yield);
                    }
                    break;
                case Crop.LBS_PER_AC:
                    if (targetUnits == Crop.KG_PER_HA) {
                        return lbsPerAcToKgPerHa(yield);
                    }
                    break;
                case Crop.BU_PER_AC:
                    if (targetUnits == Crop.LBS_PER_AC) {
                        return buPerAcToLbsPerAc(yield, cropType);
                    } else if (targetUnits == Crop.KG_PER_HA) {
                        return buPerAcToKgPerHa(yield, cropType);
                    }
                    break;
            }
        }
        return yield;
    }

    /**
     * Converts acres (imperial) to hectares (metric).
     * @param acres The area to convert
     * @return The area provided, in hectares
     */
    public double acresToHectares(double acres) {
        return acres * AC_TO_HA_FACTOR;
    }

    /**
     * Converts hectares (metric) to acres (imperial).
     * @param hectares The area to convert
     * @return The area provided, in acres
     */
    public double hectaresToAcres(double hectares) {
        return hectares * HA_TO_AC_FACTOR;
    }

    /**
     * Converts pounds to kilograms.
     * @param pounds The weight to convert
     * @return the weight provided, in kilos
     */
    public double lbsToKilos(double pounds) {
        return pounds * LBS_TO_KGS_FACTOR;
    }

    /**
     * Converts kilograms to pounds.
     * @param kilos The weight to convert
     * @return the weight provided, in pounds.
     */
    public double kilosToLbs(double kilos) {
        return kilos * KGS_TO_LBS_FACTOR;
    }

    /**
     * Converts kilos per hectare to pounds per acre.
     * @param kgPerHa The yield to be converted
     * @return The yield in imperial units.
     */
    public double kgPerHaToLbsPerAc(double kgPerHa) {
        return kgPerHa * KGS_PER_HA_TO_LBS_PER_AC_FACTOR;
    }

    /**
     * Converts pounds per acre to kilos per hectare.
     * @param lbsPerAc The yield to be converted
     * @return The yield in metric units.
     */
    public double lbsPerAcToKgPerHa(double lbsPerAc) {
        return lbsPerAc * LBS_PER_AC_TO_KG_PER_HA_FACTOR;
    }

    /**
     * Converts bushels per acre to pounds per acre.
     * @param buPerAc the yield to convert.
     * @param cropType the crop type.
     * @return the yield, in pounds per acre.
     * @throws BushelsConversionKeyNotFoundException if the crop type is not in the map of conversion factors.
     */
    public double buPerAcToLbsPerAc(double buPerAc, String cropType) throws BushelsConversionKeyNotFoundException {
        String conversionKey = bushelKeyConverter(cropType);
        if (BU_TO_LBS.containsKey(conversionKey)) {
            return buPerAc * BU_TO_LBS.get(conversionKey);
        } else {
            throw new BushelsConversionKeyNotFoundException("Key " + cropType + " not found in bushels conversion map.");
        }
    }

    /**
     * Method to handle key conversions for accessing the bushel conversion factor map.
     * @param cropType the crop type
     * @return the key used in the bushel conversion map for the given crop type, if applicable.
     */
    public String bushelKeyConverter(String cropType) {
        if (!BU_TO_LBS.containsKey(cropType)) {
            if (cropType.toLowerCase().contains("beans")) {
                return "Beans";
            } else if (cropType.toLowerCase().contains("corn") && !cropType.toLowerCase().contains("silage")) {
                return "Corn";
            } else if (cropType.toLowerCase().contains(" ")) {
                return cropType.split(" ")[0].replace(",", "");
            }
        }
        return cropType;
    }
    /**
     * Converts bushels per acre to kilos per hectare.
     * @param buPerAc the yield to convert.
     * @param cropType the crop type
     * @return the yield, in kilos per hectare
     * @throws BushelsConversionKeyNotFoundException if the crop type is not in the map of conversion factors.
     */
    public double buPerAcToKgPerHa(double buPerAc, String cropType) throws BushelsConversionKeyNotFoundException {
        String conversionKey = bushelKeyConverter(cropType);

        if (BU_TO_LBS.containsKey(conversionKey)) {
            return buPerAc * BU_TO_LBS.get(conversionKey) * LBS_PER_AC_TO_KG_PER_HA_FACTOR;
        } else {
            throw new BushelsConversionKeyNotFoundException(
                    "ERROR: Crop " + cropType + " not found in bushels conversion map."
            );
        }
    }
}
