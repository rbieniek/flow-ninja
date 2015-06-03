/**
 * 
 */
package org.flowninja.persistence.generic;

import static org.fest.assertions.api.Assertions.*;

import org.flowninja.types.generic.AccountingGroupKey;
import org.flowninja.types.generic.CollectorKey;
import org.junit.Test;

/**
 * @author rainer
 *
 */
public class ClientIDSecretGeneratorTest {

	@Test
	public void generate() {
		AccountingGroupKey groupKey = new AccountingGroupKey();
		CollectorKey collectorKey = new CollectorKey();
		
		ClientIDSecretGenerator.IDSecretPair pair = ClientIDSecretGenerator.generatePair(groupKey, collectorKey);
		
		assertThat(pair).isNotNull();
		assertThat(pair.getClientID()).hasSize(40);
		assertThat(pair.getClientSecret()).hasSize(28);

		ClientIDSecretGenerator.IDSecretPair secondPair = ClientIDSecretGenerator.generatePair(groupKey, collectorKey);
		
		assertThat(secondPair).isNotNull();
		assertThat(secondPair.getClientID()).hasSize(40);
		assertThat(secondPair.getClientSecret()).hasSize(28);
		
		assertThat(secondPair.getClientID()).isNotEqualTo(pair.getClientID());
		assertThat(secondPair.getClientSecret()).isNotEqualTo(pair.getClientSecret());
	}
}
