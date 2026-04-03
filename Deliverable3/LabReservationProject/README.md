# EECS3311 Lab Reservation System — Maven Version

## Prerequisites
- Java 11+
- Maven 3.6+

## Running Tests

### Run all tests with JaCoCo coverage
```
mvn clean test
```
Coverage report: `target/jacoco-report/index.html`

### Run only manual tests (Phase 1A/1B)
```
mvn clean test -Pmanual-suite
```
Coverage report: `target/jacoco-manual/index.html`

### Run only AI-assisted tests (Phase 3)
```
mvn clean test -Pai-suite
```
Coverage report: `target/jacoco-ai/index.html`

### Run PIT mutation testing
```
mvn clean test org.pitest:pitest-maven:mutationCoverage
```
Report: `target/pit-reports/`

## Project Structure
```
maven_version/
  pom.xml
  src/
    main/java/          — production source (model, command, data, service, ...)
    test/java/
      manual/           — Phase 1A + 1B hand-written tests (39 classes)
      ai_assisted/      — Phase 3 AI-generated tests (20 classes)
      randoop/          — Phase 2 Randoop-generated tests (2 suites)
    test/resources/
      data/             — CSV data files for integration tests
```
