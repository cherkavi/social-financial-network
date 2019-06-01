package ua.com.nmtg.private_office.common_elements.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataBase {
	/** return the list of Element for  */
	DataBaseElement[] listOfElements();
}
