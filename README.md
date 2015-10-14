Introduction
============
Deltaspike DBUnit provides integration between the Deltaspike Test-Control testing framework and the popular DBUnit project. It allows you
to setup and teardown database tables using simple annotations as well as checking expected table contents once a test completes.

This project borrows its original idea and a good deal of code from [Spring Test DBUnit](https://github.com/springtestdbunit/spring-test-dbunit).


Configuration
=============
To have Deltaspike process DBUnit annotations you must first annotate your tests with `@DatabaseTest` or `@DatabaseNoRollbackTest`.
Here are the annotations for a typical JUnit 4 test:

```
@RunWith(CdiTestRunner.class)
@DatabaseTest
```

See the Deltaspike documentation for details of the test runner.


Setup and TearDown
==================
The `@DatabaseSetup` and `@DatabaseTearDown` annotations can be used to configure database table before tests execute
and  reset them once tests have completed.

Note: Your test class needs to be annotated with `@DatabaseTest` or `@DatabaseNoRollbackTest` as described above before any
setup or tear down annotations can be used. Otherwise DBUnit annotations will be silently ignored.


Setup
=====
The `@DatabaseSetup` annotation indicates how database tables should be setup before test methods are run. The
annotation can be applied to individual test methods or to a whole class. When applied at the class level the setup
occurs before each method in the test. The annotation value references a file that contains the table DataSet used
when resetting the database. Typically this is a standard DBUnit XML file, although it is possible to load custom
formats (see below).

Here is a typical setup annotation. In this case a file named `sampleData.xml` is contained in the same package as the
test class.

    @DatabaseSetup("sampleData.xml")

By default setup will perform a `CLEAN_INSERT` operation, this means that all data from tables referenced in the
DataSet  XML will be removed before inserting new rows. The standard DBUnit operations are supported using type
attribute. See the JavaDocs for full details.


TearDown
========
The `@DatabaseTearDown` annotation can be used to reset database tables once a test has completed. As with
`@DatabaseSetup` the annotation can be applied at the method or class level. When using `@DatabaseTearDown` use the
value and type attributes in the same way as `@DatabaseSetup`.


Expected results
================
The `@ExpectedDatabase` annotation can be used to verify the contents of database once a test has completed. You would
typically use this annotation when a test performs an insert, update or delete. You can apply the annotation on a
single test method or a class. When applied at the class level verification occurs after each test method.

The `@ExpectedDatabase` annotation takes a value attribute that references the DataSet file used to verify results.
Here is a typical example:

    @ExpectedDatabase("expectedData.xml")


The `@ExpectedDatabase` annotation supports three different modes. `DatabaseAssertionMode.DEFAULT` operates as any
standard DbUnit test, performing a complete compare of the expected and actual datasets.
`DatabaseAssertionMode.NON_STRICT` will ignore tables and column names which are not specified in the expected dataset
but exist in the actual datasets. This can be useful during integration tests performed on live databases containing
multiple tables that have many columns, so one must not specify all of them, but only the 'interesting' ones.
`DatabaseAssertionMode.NON_STRICT_UNORDERED` will ignore tables and column names which are not specified in the expected
dataset but exist in the actual datasets and will also not impose any constraints on ordering.


Transactions
============
Transactions start before `@DatabaseSetup` and end after `@DatabaseTearDown` and `@ExpectedDatabase`.

If `@DatabaseTest` is used the transaction is rolled back at the end of the test run, whereas if
`@DatabaseNoRollbackTest` is used, the transaction is committed at the end of the test run.


Advanced configuration of the DbUnitTestExecutionListener
=========================================================
The `@DbUnitConfiguration` annotation can be used if you need to configure advanced options for DBUnit.

The `dataSetLoader` or `dataSetLoaderBean` attribute allows you to specify a custom loader that will be used when
reading datasets (see below). If no specific loader is specified the `FlatXmlDataSetLoader` will be used.

The `databaseOperationLookup` attribute allows you to specify a custom lookup strategy for DBUnit database operations
(see below).

Deltaspike DBUnit Annotations are currently not repeatable but if a `@DatabaseSetup` or `@DatabaseTearDown`
annotation is found at the class and method levels, they are both used to set up or tear down a database.


Writing a DataSet Loader
========================
By default DBUnit datasets are loaded from flat XML files. If you need to load data from another source you will need
to write your own DataSet loader and configure your tests to use it. Custom loaders must implement the `DataSetLoader`
interface and provide an implementation of the `loadDataSet` method. The `AbstractDataSetLoader` is also available and
provides a convenient base class for most loaders.

Here is an example loader that reads data from a CSV formatted file.

```
    public class CsvDataSetLoader extends AbstractDataSetLoader {
    	protected IDataSet createDataSet(URL resourceUrl) throws Exception {
    		return new CsvURLDataSet(resourceUrl);
    	}
    }
```

See above for details of how to configure a test class to use the loader.


Custom DBUnit Database Operations
=================================
In some situations you may need to use custom DBUnit DatabaseOperation classes. For example, DBUnit includes
`org.dbunit.ext.mssql.InsertIdentityOperation` for use with Microsoft SQL Server. The `DatabaseOperationLookup`
interface can be used to create your own lookup strategy if you need support custom operations. A
`MicrosoftSqlDatabaseOperationLookup` class is provided to support the aforementioned MSSQL operations.

See above for details of how to configure a test class to use the custom lookup.
