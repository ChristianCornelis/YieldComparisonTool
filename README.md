# Yield Comparison Tool
- A tool to compare individual farm crop productions to those of national averages provided by Statistics Canada

## Running the tool
- To run the tool, please run `gradle build` and then `gradle run`. If you are not running this via gradle in intellij,
I would suggest running via `gradle run -q --console=plain` to reduce the overlay that gradle outputs.
- Follow the on-screen instructions. Options are surrounded by parentheses.
    - Each option is selected by pressing the corresponding number. 
### Importing Producer Data
- When prompted for a **Producer** CSV to import (main menu choice 0), you must specify `src/main/resources/gjc_farms_2013_to_2019_bushels.csv`, but keep in mind that the path is relative and may need to be different.
    - This CSVs units are **BUSHELS PER ACRE**, or menu choice **2** when prompted for units.
- Some of the yields from StatsCan were left as improperly-formatted values. These will result in error messages when importing and are expected.
    
### Importing Statistics Canada Data
- When prompted for a **Statistics Canada (StatsCan)** CSV to import (main menu choice 1), you must specify `src/main/resources/statscan_yields_metric_1999_to_2019.csv`, but again, the path is relative.
    - This CSV's units are **KILOGRAMS PER HECTARE**, or menu choice **0** when prompted for units.

### Comparing Yields
- Some funky logic is at play with the yield comparator due to the way StatsCan names their crops, and how they're usually referred to. This will be refactored later on.
    - In the meantime, I would suggest using any year from 2013-2019 to compare Soybeans.
- Comparing yields in Bushels per acre is not supported at the moment. (It's not a fun conversion for hay and silage).
    - Currently comparisons are being made on the first match of a crop that was grown by a producer in the given year.
        - This will be refactored to account for farmers who grow multiple fields of the same crop annually, but for now, proves that the functionality is working correctly.
        - Comparisons can also only be done on crops with names that are one word, like 'Barley', 'Soybeans', or 'Oats'. This will be fixed later.
- The status of what data is loaded into the application is input above the menu.
- Minimal error checking was done on importing data in incorrect formats. I would suggest adhering to the above guidelines :D

## User Stories
- As a producer, I want to be able to add my production data for multiple years and crops so that I may compare them against national averages.
    - Given that I have a CSV file containing production data in the correct format, when I choose to import my production data, I am able to specify what units the data is in.
        - See the YieldComparisonToolDriver class method importFile().
    - Given that I have a CSV file containing production data in the correct format, I am able to select the file to be imported.
        - See the YieldComparisonToolDriver class method importProducerCSV().
    - Given that I have input an incorrect path to the CSV file to be imported when prompted, then I am informed of my mistake.
        - See the CSVImporter constructor, which throws an exception. This is then passed all the way up to the importProducerCSV() method.
    - Given that I do not know what units the Statistics Canada yields are in, when I choose to cancel the import, then no data is imported.
- As a producer, I want to see the margins that my crop production was higher or lower than the national average
    - Given that there is no data imported into the system, when I try to perform a a yield comparison, then it is not possible.
    - Given that there is only data from one party importer into the system, when I try to perform a yield comparison, then an error message is output indicating this. 
    - Given that both StatsCan and producer yields are loaded, then I am able to select a unit to compare yields of certain crops in.
        - See the YieldComparisonToolDriver, which performs this functionality.
    - Given that I decided on a specific year to compare yields in, when I enter the year, then I am able to choose what crops to compare.
        - See the YieldComparator class, which is utilized by compareYields() in YieldComparisonToolDriver.
    -Given that I decided on a specific year to compare yields in, when I enter the year and it is not present in both sets of data, I am notified of valid years that can be input.
    - Given that I decided on a specific crop to compare yields against, when I enter a crop type that is present in my production data and StatsCan data, then I am able to see the differences in the yields.
    - Given that I decided on a specific crop to compare yields against, when I enter a crop that is not present in both my production data and StatsCan data, then I am notified that the data is not present to compare this crop.
- As an administrator, I want to be able to add Statistics Canada records of crop production to increase the data available for producers to compare against.
    - Given that I have downloaded crop production records from Statistics Canada and that data is formatted correctly, when I choose to import these records, I am able to specify what units the data is in.
         - See the YieldComparisonToolDriver class method importFile().
    - Given that I have a CSV file containing StatsCan data in the correct format, I am able to select the file to be imported.
         - See the YieldComparisonToolDriver class method importStatsCanCSV().
    - Given that I have input an incorrect path to the CSV file to be imported when prompted, then I am informed of my mistake.
            - See the CSVImporter constructor, which throws an exception. This is then passed all the way up to the importStatsCanCSV() method.
    - Given that I do not know what units the Statistics Canada yields are in, when I choose to cancel the import, then no data is imported.

## Analysis Of Implementation
### Single Responsibility Principle
- I attempted to adhere to the single responsibility principle by ensuring that each method had one single task it was meant to perform. There are some cases, such as in the YieldComparisonToolDriver,
where some side effects, such as IO, are in place; however, I decided to keep these in place so as to not go so in depth as to make a wrapper class on printing to stdout itself.
- By breaking down the implementation of the Importer into an interface, abstract class, and sub-classes (that were concrete classes) I was able to extract a lot of "business logic" into subclasses
to avoid breaking this principle.
- The converter class is a good example of Single Responsibility. Each method has one specific task, which may or may not rely on other methods in order to perform said task.
- 

### Open-Close Principle
- This principle was quite challenging to adhere to considering the data I was dealing with. I attempted to keep producer data encapsulated in the current state of the application to
enforce the fact that producers should not be capable of seeing or using other producers' data.
- Keeping the above in mind, I provided getters and setters for the vast majority of instance variables defined in my classes to make them as extendable as possible.
- The only instance and class variables I made the choice to not provide these methods for were specific, private static constants such as those found in the Converter class, public static constants found in
 the Crop class and PromptHelper class (as they can be accessed directly), and the Producer yield maps found inn the YieldComparator and YieldComparisonToolDriver.
 - I attempted to abstract my class definitions as much as possible by defining interfaces that were then used by classes. This reinforced extendability while maintaining specific implementation
 details at the lowest level possible.
 
 ## Please Read! - Git merging habits
 - In a haste to merge one of my branches I ended up squashing all commits and deleting my source branch.
 - You can see this merge request on Gitlab by going to Merge Requests -> Merged -> StatsCanImporter
 - I opted to NOT squash all commits and leave source branches for transparency after this point.