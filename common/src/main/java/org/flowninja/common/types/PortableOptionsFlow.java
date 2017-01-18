/**
 *
 */
package org.flowninja.common.types;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author rainer
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PortableOptionsFlow {

    private String peerAddress;
    private Header header;
    private String uuid;
    private List<PortableScopeFlowRecord> scopes;
    private List<PortableFlowValueRecord> records;
}
