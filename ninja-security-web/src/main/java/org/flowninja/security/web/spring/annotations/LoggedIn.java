/**
 * 
 */
package org.flowninja.security.web.spring.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.flowninja.security.web.spring.controllers.LoggedInControllerAdvice;

/**
 * @author rainer
 * 
 * This annotation is used on RESTful controllers to assert that the controller is called with a proper authentication context.
 * It works both in administrator and plain user contexts. It is handled by {@link LoggedInControllerAdvice}
 * 
 * @see LoggedInControllerAdvice
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LoggedIn {

}
