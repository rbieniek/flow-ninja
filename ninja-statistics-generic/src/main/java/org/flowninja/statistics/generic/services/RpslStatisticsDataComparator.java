/**
 * 
 */
package org.flowninja.statistics.generic.services;

import java.util.Comparator;

/**
 * @author rainer
 *
 */
public class RpslStatisticsDataComparator implements
		Comparator<RpslStatisticsData> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(RpslStatisticsData o1, RpslStatisticsData o2) {
		if(o1 == null || o1.getStamp() == null)
			throw new IllegalArgumentException();
		if(o2 == null || o2.getStamp() == null)
			throw new IllegalArgumentException();
		
		return o1.getStamp().compareTo(o2.getStamp());
	}

}
