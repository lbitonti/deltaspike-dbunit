# Frequently Asked Questions

## Is this project in a Maven repository?
Yes it is. see http://mvnrepository.com/artifact/com.github.deltaspikedbunit/deltaspike-dbunit for details.

## Is there a way to clear out the Database when tests are complete?
The recommendation from DBUnit is that you have a good database setup and don't cleanup (see http://www.dbunit.org/bestpractices.html#nocleanup).  That being said there are occasions where you might want to cleanup your database after every test.

There are a couple of strategies that you can use:

1) Use the @DatabaseTest annotation and rollback after each test.  See the section on Transactions in the project [readme](index.html).  This approach can work work for many tests, however, sometimes you may not want to rollback transactions to be sure that no exceptions would have been raised on the commit.

2) Use the @DatabaseNoRollbackTest on the test class with the @DatabaseTearDown annotation on the test class or test method and provide a reset DBUnit XML file.  Creating a reset script can often be difficult if you have foreign key constraints (see http://www.andrewspencer.net/2011/solve-foreign-key-problems-in-dbunit-test-data/).  Any empty table element tells DBUnit to delete all data, so a reset script is generally a list of tables in the order that they can be deleted without causing foreign key constraints, e.g.:

    <inventory_category/>
    <inventory_item/>

## How similar is this project to Spring Test DBUnit
Very. I've tried to reuse as much of [Spring Test DBUnit](https://springtestdbunit.github.io/spring-test-dbunit/) as I could as it's a very good project and I don't like to reinvent the wheel. The only parts I rewrote are the ones related to Spring as those are (obviously) incompatible with CDI/Deltaspike.