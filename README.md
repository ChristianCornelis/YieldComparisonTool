# Yield Comparison Tool
- A tool to compare individual farm crop productions to those of national averages provided by Statistics Canada

## Running the tool
- To run the tool, please run `gradle build` and then `gradle run`. If you are not running this via gradle in intellij,
I would suggest running via `gradle run -q --console=plain` to reduce the overlay that gradle outputs.
- `gradle check` will lint the application.
- I would HIGHLY reccomend you run this outside of intellij - if an output message containing `ERROR`, it can sometimes freeze the console.
- Follow the on-screen instructions. Options are surrounded by parentheses.
    - Each option is selected by pressing the corresponding number. 
    - Some options are not implemented.
    - You will be prompted to enter a producer name when the application starts. The only reserved usernames are `admin` and `Admin`. This will result in no producer name being set if you do this.
- Tests can be run with `gradle test`

## Improvements Based on M1 Feedback
- Implemented the ability to compare lower-case-specified crops!
- Refactored the Driver class to better maintain single-responsibility.
- Ensured that method names were all verbs
- Ensured that instance variables had meaningful names
- Added a glossary to this document to aid in the understanding of constant nomenclature
- Organized source code into specific packages to make their uses clear.

### Importing Producer Data
- When prompted for a **Producer** CSV to import (main menu choice 0), you must specify `src/main/resources/producer_lite.csv`, but keep in mind that the path is relative and may need to be different.
    - This CSVs units are **BUSHELS PER ACRE**, or menu choice **2** when prompted for units.
    
### Importing Statistics Canada Data
- When prompted for a **Statistics Canada (StatsCan)** CSV to import (main menu choice 1), you must specify `src/main/resources/statscan_lite.csv`, but again, the path is relative.
    - This CSV's units are **KILOGRAMS PER HECTARE**, or menu choice **0** when prompted for units.
    - Some of the yields from StatsCan were left as improperly-formatted values. These will result in error messages when importing and are expected.

### Comparing Yields
- Some funky logic is at play with the yield comparator due to the way StatsCan names their crops, and how they're usually referred to. This will be refactored later on.
    - In the meantime, I would suggest using any year from 2018-2019 to compare Soybeans.
- Comparing yields in Bushels per acre is not supported at the moment. (It's not a fun conversion for hay and silage). **Update** -  It's looking like this might be forever-deprecated. If it is selected it will make you choose again until another option is chosen.
    - Currently comparisons are being made on the first match of a crop that was grown by a producer in the given year.
        - Comparisons on crops with names that are more than one word long are now supported, but it a super gross way - spaces must be replaced with underscores. So "Corn for grain" would be replaced with "Corn_for_grain".
            - This is due to some wonky scanner behavior when trying to parse more than one string at a time, but it won't matter once this is done via a web interface :)
- The status of what data is loaded into the application is input above the menu.
- Assume no error checking was done on importing data in incorrect formats. I would suggest adhering to the above guidelines :D Also, see SCHEMA.md for data schemas.
- Lowercase crop names should work for comparisons now!
- Tested crops include Faba beans (with "faba_beans") Corn (with "corn_for_grain" - again, StatsCan naming conventions make it hard), and soybeans.

### Current State of the Application
- The App (unless people play around with it during review - pls no) will be populated with producer and Statistics Canada data for 2018 and 2019.
    - Producer data will be input by the Producer "Tester", so if you want to have that data pre-loaded you MUST enter that as the producer name at login.
- By logging in with "admin" or "Admin" you simply don't have the ability to import producer data. I was going to extend this so that producers couldn't import StatsCan data, but didn't have time.
- No testing was done for if Firebase query quotes are exceeded, or if database connections fail!

## Caching Behavior / Database Limitations
- When the application starts up, it will attempt to fill the local caches. If the producer using the application has imported data before, then that imported data will be cached locally and will be
  immediately available for comparisons. Any Statistics Canada data that has previously been imported is also cached at startup.
- With Firebase having only 50,000 read operations per day, I opted to implement caching behavior that would help save some queries. REALLLLLY lazy caching was attempted.
- When importing data, if the local cache (be it StatsCan or producer), will check if the record exists for the given year. If it DOES, then the record is NOT overwritten - local caches and the database are left untouched.
    - Note that this "exists" check is done on the FULL record being imported - if the yield is slightly different it will be overwritten.
- Note that because the database is limited to 50,000 reads/day, smaller datasets have been pre-loaded into the state. If you're gonna play around with the application, be mindful of this pls :)
- Note that when you attempt to delete producer records the producer cache is invalidated. Keep this in mind, as it will entail a complete re-load of the state, and 100s of read operations!
- For usage perspective I was able to burn through about 3000 read operations in 15 minutes of intense testing using the `lite` CSVs.

## Glossary of Terms and Constants
- Yields, weights, and areas must be converted in order for comparisons to be done in either metric or imperial units.
- This section aims to explain some of these units and conversions. Constants can be found in `src/main/java/project/Converter.java`

| Term | Explanation |
| ---- | --------- |
| Bushel | An imperial measurement equivalent to 64 US pints (or 32.5 liters) commonly used to measure the yield of crops. This amount of goods in a bushel varies based on crop size and density. |
| Acre | An imperial measurement of area equivalent to 66 x 660 feet, or 43 560 square feet. Equivalent to 4047 square meters, or about 0.4 hectares. |
| Hectare | A metric unit of square measurement that is 10 000 square meters, or 100m x 100m. Equivalent to 2.471 acres. |

| Constant | Explanation |
| -------- | --------- |
| `KGS_PER_HA_TO_LBS_PER_AC_FACTOR` | The factor used to convert yields between the metric units, kilograms per hectare, to the imperial units pounds per acre. |
| `LBS_PER_AC_TO_KG_PER_HA_FACTOR` | The factor used to convert yields between the imperial units, pounds per acre, to the metric units, kilograms per acre.|
| `KGS_TO_LBS_FACTOR` | The factor to convert weights from kilograms (metric) to pounds (imperial). |
| `LBS_TO_KGS_FACTOR` | The factor to convert weights from pounds (imperial) to kilograms (metric). |
| `HA_TO_AC_FACTOR` | The factor to convert areas measured in hectares (metric) to acres (imperial). |
| `AC_TO_HA_FACTOR` | The factor to convert areas measured in acres (imperial) to hectares (metric). |

# M2 Documentation
## M2 User Stories
- As a producer, I want to be able to import my production data once and have it stored between application uses so I do not have to import data every time the app is used.
    - Given that I have imported a CSV file successfully, when I quit the application, and re-open it my data is stored for use at a later time.
    - Given that I have not imported a CSV file successfully, when I quit the application and re-open it, no producer data is populated in the application.
- As a producer, I want to be able to delete all imported records from a given year.
    - Given that I have imported a CSV file successfully, when I specify a year to delete records from, all yields are removed from the application for that year.
    - Given that I have not imported a CSV file successfully, when I specify a year to delete records from, nothing happens.
- As an administrator, I want to be able to import StatsCan data once and have it stored between application uses so I do not have to import data every time the app is used.
    - Given that I have imported a CSV file successfully, when I quit the application, and re-open it my data is stored for use at a later time.
    - Given that I have not imported a CSV file successfully, when I quit the application and re-open it, no producer data is populated in the application.
    
## Analysis of Implementation
### Liskov Substitution Principle
- I attempted to adhere to this principle with my data classes - Crop and Farm. By refactoring Farm to extend Crop, I was able to utilize this principle by defining methods usable with both object
  types. This sped up implementation, reduced code bloat, and removed duplicate code in some cases.
- `project.database.DatabaseClient.convertRecordsToMap` is a good example of this (albeit some weird casting had to be done - thanks checkstyle).
- Another example of when this principle was utilized is in `project.comparators.YieldComparator.retrieveYield`, which is used for both StatsCan and Producer data.

### Interface Segregation
- This principle made me really think about my class implementation when approaching this deliverable - especially in the case of my DatabaseClient class. As this class entailed much of the functionality introduced
in this deliverable, it is where I focused most on this principle. Instead of having multiple sub-classes that performed small tasks related to producer and statscan data operations, I opted to make the single
DatabaseClient class that implemented multiple interfaces. Becauase of the design of the interfaces, the ability to spin off multiple sub-clients is readily available if the need were to arise.
-`project.database` contains some examples of interface segregation, as well as `project.importers`.

### Class Length
- Class length was a tricky problem to tackle this milestone. With the implementation of my Driver class and the way it was structured, I had to really think about how to break it up without introducing a 
whole bunch of unnecessary complexity.
- I opted to introduce the `project.helpers` package, which contains helper methods and handlers for input, record deletion, comparing yields, and importing.
- In this way, the core functionality of the project lives outside of the helpers package, which will become deprecated once a web app is put in front of this project.

### Unit Tests
- Unit tests were completed on concrete classes that did not handle user input, database operations, or file parsing. I realize that this may not quite be 50% code coverage, but
I'll be honest - I didn't want to mock out database connections, CSV importers libraries, and standard input.
- All tests live in the `src/test/java/project` package, which has a structure that mirrors that of the main project package (for fleshed-out tests).
- A stage was added to the CI to run the tests.

# M1 Documentation
## M1 User Stories
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

### Open-Closed Principle
- This principle was quite challenging to adhere to considering the data I was dealing with. I attempted to keep producer data encapsulated in the current state of the application to
enforce the fact that producers should not be capable of seeing or using other producers' data.
- Keeping the above in mind, I provided getters and setters for the vast majority of instance variables defined in my classes to make them as extendable as possible.
- The only instance and class variables I made the choice to not provide these methods for were specific, private static constants such as those found in the Converter class, public static constants found in
 the Crop class and PromptHelper class (as they can be accessed directly), and the Producer yield maps found inn the YieldComparator and YieldComparisonToolDriver.
 - I attempted to abstract my class definitions as much as possible by defining interfaces that were then used by classes. This reinforced extendability while maintaining specific implementation
 details at the lowest level possible.
 
 ## References
 - The Google Cloud Firestore docs were of huge help during this milestone: https://firebase.google.com/docs/firestore
    - used this documentation for querying the database for storage, retrieval, and deletion, as well as hooking up to Firebase from my application

