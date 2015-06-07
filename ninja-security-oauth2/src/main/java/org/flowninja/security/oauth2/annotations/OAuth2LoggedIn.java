/**
 * 
 */
package org.flowninja.security.oauth2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author rainer
 * 
 * This annotation is used on RESTful controllers to assert that the controller is called with a proper authentication context.
 * It works both in administrator and plain user contexts. It is handled by {@link OAuth2LoggedInControllerAdvice}
 * 
 * @see OAuth2LoggedInControllerAdvice
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OAuth2LoggedIn {

}
