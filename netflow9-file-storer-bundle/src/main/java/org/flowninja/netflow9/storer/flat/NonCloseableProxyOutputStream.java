/**
 * 
 */
package org.flowninja.netflow9.storer.flat;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.ProxyOutputStream;

/**
 * @author rainer
 *
 */
public class NonCloseableProxyOutputStream extends ProxyOutputStream {

	public NonCloseableProxyOutputStream(OutputStream proxy) {
		super(proxy);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.io.output.ProxyOutputStream#close()
	 */
	@Override
	public void close() throws IOException {
	}

	
}
