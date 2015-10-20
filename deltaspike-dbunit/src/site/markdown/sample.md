Quick Start Example
===================

In this example we show how to test an Eclipselink JPA project using a Derby in-memory database.  The example project is available [here](https://github.com/lbitonti/deltaspike-dbunit/tree/master/deltaspike-dbunit-sample).

Dependencies
============

This project is built using Apache Maven.  Here is the complete POM file with all of the dependencies needed:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.sonatype.oss</groupId>
		<artifactId>oss-parent</artifactId>
		<version>9</version>
		<relativePath />
	</parent>
	<groupId>com.github.lbitonti</groupId>
	<artifactId>deltaspike-dbunit-sample</artifactId>
	<version>0.5.0</version>
	<packaging>jar</packaging>
	<name>Deltaspike DBUnit Sample</name>
	<description>Sample application for deltaspike-dbunit project</description>
	<licenses>
		<license>
			<name>Apache 2</name>
			<url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
			<distribution>repo</distribution>
		</license>
	</licenses>
	<scm>
		<url>https://github.com/lbitonti/deltaspike-dbunit</url>
		<connection>scm:git:git://github.com/lbitonti/deltaspike-dbunit.git</connection>
		<developerConnection>scm:git:git@github.com:lbitonti/deltaspike-dbunit.git</developerConnection>
	  <tag>HEAD</tag>
  </scm>
	<prerequisites>
		<maven>3.0.3</maven>
	</prerequisites>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<java.version>1.7</java.version>
		<org.eclipse.persistence-version>2.5.2</org.eclipse.persistence-version>
		<deltaspike.version>1.2.1</deltaspike.version>
	</properties>
	<developers>
		<developer>
			<name>Luigi Bitonti</name>
			<url>https://github.com/lbitonti</url>
		</developer>
	</developers>
	<build>
		<plugins>
			<plugin>
				<artifactId>maven-clean-plugin</artifactId>
				<version>2.5</version>
			</plugin>
			<plugin>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.1</version>
				<configuration>
					<compilerVersion>${java.version}</compilerVersion>
					<source>${java.version}</source>
					<target>${java.version}</target>
					<testSource>1.7</testSource>
					<testTarget>1.7</testTarget>
				</configuration>
			</plugin>
			<plugin>
				<artifactId>maven-deploy-plugin</artifactId>
				<version>2.8.2</version>
			</plugin>
			<plugin>
				<artifactId>maven-install-plugin</artifactId>
				<version>2.5.2</version>
			</plugin>
			<plugin>
				<artifactId>maven-jar-plugin</artifactId>
				<version>2.5</version>
			</plugin>
			<plugin>
				<artifactId>maven-release-plugin</artifactId>
				<version>2.5</version>
				<dependencies>
					<dependency>
						<groupId>org.apache.maven.scm</groupId>
						<artifactId>maven-scm-provider-gitexe</artifactId>
						<version>1.9.1</version>
					</dependency>
				</dependencies>
				<executions>
					<execution>
						<id>default</id>
						<goals>
							<goal>perform</goal>
						</goals>
						<configuration>
							<pomFileName>deltaspike-dbunit/pom.xml</pomFileName>
						</configuration>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<artifactId>maven-resources-plugin</artifactId>
				<version>2.6</version>
			</plugin>

			<plugin>
			    <groupId>org.apache.maven.plugins</groupId>
			    <artifactId>maven-surefire-plugin</artifactId>
			    <version>2.18.1</version>
			    <!--
			    <configuration>
			        <includes>
				    <include>**/*Test*.java</include>
				</includes>
			    </configuration>
			    -->
			</plugin>
			<plugin>
			    <groupId>org.carlspring.maven</groupId>
			    <artifactId>derby-maven-plugin</artifactId>
			    <version>1.10</version>
			    <configuration>
			        <derbyHome>${project.build.directory}/derby</derbyHome>
				<port>1529</port>
				<username>APP</username>
				<password>APP</password>
				<connectionURL>jdbc:derby://localhost:1529/InventoryTestDB;create=true</connectionURL>
				<connectionURLShutdown>jdbc:derby:;shutdown=true</connectionURLShutdown>
				<debugStatements>true</debugStatements>
				<skip>false</skip>
			    </configuration>
			    <executions>
			        <execution>
				    <id>start-derby-test</id>
				    <phase>test-compile</phase>
				    <goals>
				        <goal>start</goal>
				    </goals>
				</execution>
			    </executions>
			</plugin>

			<plugin>
				<artifactId>maven-source-plugin</artifactId>
				<version>2.3</version>
				<executions>
					<execution>
						<id>attach-sources</id>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<artifactId>maven-javadoc-plugin</artifactId>
				<version>2.8.1</version>
				<executions>
					<execution>
						<id>attach-javadocs</id>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>com.github.github</groupId>
				<artifactId>site-maven-plugin</artifactId>
				<version>0.10</version>
				<configuration>
					<message>Creating site for ${project.version}</message>
				</configuration>
				<executions>
					<execution>
						<goals>
							<goal>site</goal>
						</goals>
						<phase>site</phase>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>

	<dependencies>

          <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>eclipselink</artifactId>
            <version>${org.eclipse.persistence-version}</version>
          </dependency>

          <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>org.eclipse.persistence.jpa</artifactId>
            <version>${org.eclipse.persistence-version}</version>
            <scope>compile</scope>
          </dependency>

		  <dependency>
            <groupId>javax.inject</groupId>
            <artifactId>javax.inject</artifactId>
            <version>1</version>
          </dependency>

          <dependency>
            <groupId>javax.enterprise</groupId>
            <artifactId>cdi-api</artifactId>
            <version>1.2</version>
            <scope>provided</scope>
          </dependency>

          <dependency>
            <groupId>javax.transaction</groupId>
            <artifactId>javax.transaction-api</artifactId>
            <version>1.2</version>
          </dependency>

          <dependency>
            <groupId>org.apache.deltaspike.core</groupId>
            <artifactId>deltaspike-core-api</artifactId>
            <version>${deltaspike.version}</version>
            <scope>compile</scope>
          </dependency>
          <dependency>
            <groupId>org.apache.deltaspike.core</groupId>
            <artifactId>deltaspike-core-impl</artifactId>
            <version>${deltaspike.version}</version>
            <scope>runtime</scope>
          </dependency>

          <dependency>
            <groupId>org.apache.deltaspike.modules</groupId>
            <artifactId>deltaspike-jpa-module-api</artifactId>
            <version>${deltaspike.version}</version>
            <scope>compile</scope>
          </dependency>
          <dependency>
            <groupId>org.apache.deltaspike.modules</groupId>
            <artifactId>deltaspike-jpa-module-impl</artifactId>
            <version>${deltaspike.version}</version>
            <scope>runtime</scope>
          </dependency>

        <dependency>
            <groupId>org.apache.deltaspike.cdictrl</groupId>
            <artifactId>deltaspike-cdictrl-api</artifactId>
            <version>${deltaspike.version}</version>
            <scope>compile</scope>
        </dependency>

        <dependency>
            <groupId>org.apache.deltaspike.modules</groupId>
            <artifactId>deltaspike-data-module-api</artifactId>
            <version>${deltaspike.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.deltaspike.modules</groupId>
            <artifactId>deltaspike-data-module-impl</artifactId>
            <version>${deltaspike.version}</version>
            <scope>runtime</scope>
        </dependency>

		<dependency>
            <groupId>org.javassist</groupId>
            <artifactId>javassist</artifactId>
            <version>3.18.1-GA</version>
        </dependency>

        <dependency>
            <groupId>com.github.lbitonti</groupId>
            <artifactId>deltaspike-dbunit</artifactId>
            <version>0.5.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.dbunit</groupId>
            <artifactId>dbunit</artifactId>
            <version>2.5.1</version>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>junit</groupId>
                    <artifactId>junit</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.hamcrest</groupId>
            <artifactId>hamcrest-all</artifactId>
            <version>1.3</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.derby</groupId>
            <artifactId>derby</artifactId>
            <version>10.10.2.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.derby</groupId>
            <artifactId>derbyclient</artifactId>
            <version>10.10.2.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.deltaspike.modules</groupId>
            <artifactId>deltaspike-test-control-module-api</artifactId>
            <version>${deltaspike.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.deltaspike.modules</groupId>
            <artifactId>deltaspike-test-control-module-impl</artifactId>
            <version>${deltaspike.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.jboss.weld.se</groupId>
            <artifactId>weld-se</artifactId>
            <version>2.2.16.Final</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.deltaspike.cdictrl</groupId>
            <artifactId>deltaspike-cdictrl-weld</artifactId>
            <version>${deltaspike.version}</version>
            <scope>test</scope>
        </dependency>

	</dependencies>

</project>
```

Entity
======

For this simple project we create 2 entities with a one-to-many relationship: "InventoryCategory" and "InventoryItem".  We also declare a repository with a query to use in the application.

```
@Entity
@Table(name = "inventory_category")
public class InventoryCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Version
    @Column(name = "version")
    private int version;
    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;
    @Column(name = "category_description")
    private String categoryDescription;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<InventoryItem> items = new HashSet<>();

	// getters, setters and some other methods skipped for brevity
	// see: https://github.com/lbitonti/deltaspike-dbunit/tree/master/deltaspike-dbunit//Users/luigi/sbattimenti/github/deltaspike-dbunit/deltaspike-dbunit-sample/src/main/java/com/github/deltaspikedbunit/sample/inventory/entity/InventoryCategory.java
}
```

```
@Entity
@Table(name = "inventory_item")
public class InventoryItem {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int id;
   @Version
   @Column(name = "version")
   private int version;
   @Column(name = "item_name")
   private String itemName;
   @Column(name = "item_description")
   private String itemDescription;
   @ManyToOne
   @JoinColumn(name = "category_id")
   InventoryCategory category;

	// getters, setters and some other methods skipped for brevity
	// see: https://github.com/lbitonti/deltaspike-dbunit/tree/master/deltaspike-dbunit//Users/luigi/sbattimenti/github/deltaspike-dbunit/deltaspike-dbunit-sample/src/main/java/com/github/deltaspikedbunit/sample/inventory/entity/InventoryItem.java
}
```

```
@Repository
public abstract class InventoryCategoryRepository extends AbstractEntityRepository<InventoryCategory, Integer> {

    @Query(value = "select ic from InventoryCategory ic where ic.categoryName = :categoryName",
	   singleResult = SingleResultType.OPTIONAL)
    public abstract InventoryCategory findByName(@QueryParam("categoryName") String categoryName);

}
```

We need to setup a persistence.xml file for Eclipselink to use:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.0" xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd">

    <persistence-unit name="inventoryTest" transaction-type="RESOURCE_LOCAL">
        <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
        <exclude-unlisted-classes>false</exclude-unlisted-classes>

        <properties>
            <property name="eclipselink.target-database" value="Derby"/>

            <property name="javax.persistence.jdbc.driver" value="org.apache.derby.jdbc.ClientDriver" />
            <property name="javax.persistence.jdbc.url" value="jdbc:derby://localhost:1529/InventoryTestDB;create=true" />
            <property name="javax.persistence.jdbc.user" value="app" />
            <property name="javax.persistence.jdbc.password" value="app" />

            <property name="eclipselink.ddl-generation" value="drop-and-create-tables"/>
            <property name="eclipselink.ddl-generation.index-foreign-keys" value="true"/>

            <property name="eclipselink.logging.level"      value="INFO"/>
            <property name="eclipselink.logging.level.sql"  value="ALL"/>
            <property name="eclipselink.logging.parameters" value="true"/>

            <property name="eclipselink.deploy-on-startup" value="true"/>
        </properties>
    </persistence-unit>

</persistence>
```

We also need to setup a beans.xml file for CDI:

```xml
<beans>
    <alternatives>
        <class>org.apache.deltaspike.jpa.impl.transaction.EnvironmentAwareTransactionStrategy</class>
    </alternatives>
    <interceptors>
        <class>org.apache.deltaspike.jpa.impl.transaction.TransactionalInterceptor</class>
    </interceptors>
</beans>
```

Here we have enabled a TransactionalInterceptor so that transactions can be used declaratively in our application.


Service
=======

We need to have something to test so we create a simple service that lets us create and search InventoryCategory entities using the repository from above:

```
@Transactional
public class InventoryService {

    @Inject
    InventoryCategoryRepository inventoryRepository;


    public InventoryCategory getCategory(String name) {
		if (name == null) {
			return null;
		}
		return inventoryRepository.findByName(name);
    }

    public List<InventoryCategory> getAllCategories() {
        return inventoryRepository.findAll();
    }

    public void addCategory(InventoryCategory category) {
		if (category != null) {
			if (category.getCategoryName() == null || category.getCategoryName().trim().length() == 0) {
                throw new IllegalArgumentException("Category name cannot be blank.");
			}
            InventoryCategory existingCategory = inventoryRepository.findByName(category.getCategoryName());
            if (existingCategory != null) {
                throw new IllegalArgumentException("Category name is a duplicate.");
            }
			inventoryRepository.save(category);
		}
    }

}
```


Testing
=======

To make sure that our service is working we need to create a JUnit test.  Here is the basic structure of the test class:

```
@RunWith(CdiTestRunner.class)
@DatabaseTest
@DatabaseSetup("../setup-db.xml")
public class InventoryServiceTest {

    @Inject
    private InventoryService inventoryService;


    @Test
    public void getCategoryShouldFindDefaultOne() {
	    InventoryCategory category = inventoryService.getCategory("default_category");
        Assert.assertEquals("default description", category.getCategoryDescription());
    }
}
```


Our test method checks that the service can find entities.


To make sure that the test passes we need to insert some database data.  This is where DBUnit comes in.  We can use the @DatabaseSetup annotation on the test method or test class to configure the database from a flat XML file.  The xml file follows the standard DBUnit conventions:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<dataset>

  <inventory_category id="1001" version="1" category_name="default_category" category_description="default description"/>

  <inventory_item id="2001" version="1" item_name="default_item_1" item_description="default desc 1" category_id="1001"/>
  <inventory_item id="2002" version="1" item_name="default_item_2" item_description="default desc 2" category_id="1001"/>

</dataset>
```

DBUnit executes before the test method runs to insert appropriate data into the database.  As a result the test should pass.


Testing expected data
=====================

As well as configuring the database before a test runs it is also possible to verify database set after the test completes. The method to test if remove works can use the @ExpectedDatabase annotation.  This will use DBUnit to ensure that the database contains expected data after the test method has finished.

```
@RunWith(CdiTestRunner.class)
@DatabaseTest
@DatabaseSetup("../setup-db.xml")
public class InventoryServiceTest {

    @Inject
    private InventoryService inventoryService;

    @Test
    @ExpectedDatabase(value = "extra_and_default_category-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void getCategoriesAfterExtraAddShouldFindBoth() {
        List<InventoryCategory> categories = inventoryService.getAllCategories();
        Assert.assertEquals(1, categories.size());
        Assert.assertEquals("default description", categories.get(0).getCategoryDescription());

        InventoryCategory category = new InventoryCategory("extra_category", "extra description");
        InventoryItem item1 = new InventoryItem("extra_item_1", "extra desc 1");
        InventoryItem item2 = new InventoryItem("extra_item_2", "extra desc 2");
        category.addItems(item1, item2);
        inventoryService.addCategory(category);

        categories = inventoryService.getAllCategories();
        Assert.assertEquals(2, categories.size());
    }
}
```


Summary
=======

This quick introduction has shown how deltaspike-dbunit can be used to setup a database and verify expected content using simple annotations.

