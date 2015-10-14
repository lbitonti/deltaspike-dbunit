package com.github.deltaspikedbunit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
//import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates how to put a database into a know state after tests have run. This annotation can be
 * placed on a class or on methods. When placed on a class the setup is applied after each test
 * method is executed.
 *
 * @author Luigi Bitonti
 * @author Phillip Webb
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DatabaseTearDown {

    /**
     * Determines the type of {@link DatabaseOperation operation} that will be used to reset the database.
     * @return The type of operation used to reset the database
     */
    DatabaseOperation type() default DatabaseOperation.CLEAN_INSERT;

    /**
     * Provides the locations of the datasets that will be used to reset the database.
     * @return The dataset locations
     */
    String[] value();

}
