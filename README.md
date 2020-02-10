# Yield Comparison Tool
- A tool to compare individual farm crop productions to those of national averages provided by Statistics Canada

## User Stories
- As a producer, I want to be able to add my production data for multiple years and crops so that I may compare them against national averages.
    - Given that I have a CSV file containing production data in the correct format, when I choose to import my production data, I am able to specify what units the data is in.
    - Given that I know what units my yields are in, when I chose the units, the data is imported into the program.
    - Given that I do not know what units my yields are in, when I choose that I do not know  them, the import is cancelled.
- As a producer, I want to see the margins that my crop production was higher or lower than the national average
    - Given that I decided on a a specific year to compare yields in, when I enter the year, then I am able to choose what crops to compare.
    - Given that I decided on a specific crop to compare yields against, when I enter a crop type that is present in my production data and StatsCan data, then I am able to see the differences in the yields.
    - Given that I decided on a specific crop to compare yields against, when I enter a crop that is not present in both my production data and StatsCan data, then I am notified that the data is not present to compare this crop.
- As an administrator, I want to be able to add Statistics Canada records of crop production to increase the data available for producers to compare against.
    - Given that I have downloaded crop production records from Statistics Canada and that data is formatted correctly, when I choose to import these records, I am able to specify what units the data is in.
    - Given that I know what units the Statistics Canada yields are in, when I chose the units, the data is imported into the program.
    - Given that I do not know what units the Statistics Canada yields are in, when I choose that I do not know  them, the import is cancelled.

### User Stories for administrator Role: 
- As an administrator, I want to be able to remove Statistics Canada records of crop production in order to remove any data that is deemed no longer necessary to be included in the system.
- As an administrator, I want to be able to upload CSVs obtained directly from Statistic Canada's website without removing any of the bloat (footnotes, comments, etc...) that comes attached to them, in order to expedite the data ingestion process.

### User Stories for producer Role:
- As a producer, I want to see whether my crop production was higher or lower than the national average so I may gauge my operations's productivity at a high-level.
- As a producer, I want to see the margins that my crop production was higher or lower than the national average so I may determine if a lower or higher yield on my operation was als seen nationally. 
- As a producer, I want to be able to remove production data for multiple years and crops so that I may fix corrupt, incorrect, or unnecessary data that was added to the application.
- As a producer, I want to be able to filter on different years and crop types so that I can easily investigate the trends I am most interested in.
- As a producer, I want to be able save the application's state so that I do not have to upload my production data every time I start the application.
- This could be done via object serialization if database implementation is not feasible for M1.
