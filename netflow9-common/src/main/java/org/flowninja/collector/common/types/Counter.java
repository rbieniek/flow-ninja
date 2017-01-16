/**
 *
 */
package org.flowninja.collector.common.types;

/**
 * This interface models a counter-like numeric object which is exported in flow records
 *
 * @author rainer
 *
 */
public interface Counter {

    /**
     *
     * @return
     */
    String printableValue();

    /**
     *
     * @return
     */
    Number value();
}
