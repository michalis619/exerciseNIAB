# Harvest Data Processor
This is a project to solve the Harvest data exercise.
It contains Java classes for processing data according to the instructions. It can process clean data files, data files with errors, and data files that are processed together with an override file.
It also contains a non-functional login webpage.  
No frameworks were used for the Java side of the project. Where for the webpage Boostrap was used.

## Features

- Process clean data files.
- Handle data files with errors.
- Override data files with additional information.

## Usage

### Processing Data Files

The `Main` class includes examples of processing each data file for each case. It will be easier for testing to comment out all the cases except the one you are testing before running.

### Unit Tests

Unit tests are included in the project and can be found in the `test/java` directory.

## Building the Project

Before running the project, ensure you have Maven installed and then run the following command to build the project:
```sh
mvn clean install
```

All the Java classes are inside  directory src/main/java, apart from the tests which are in src/test/java. The webpage is inside the src/main/webPage directory and all the data files and the logs file are in the src/main/resources directory.
