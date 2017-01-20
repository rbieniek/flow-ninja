package org.flowninja.common.types;

import java.math.BigInteger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class PayloadCounters {

    private BigInteger inBytes;
    private BigInteger inPkts;
    private BigInteger outBytes;
    private BigInteger outPkts;
    private BigInteger mulDstBytes;
    private BigInteger mulDstPkts;
    private BigInteger totalBytesExp;
    private BigInteger totalPktsExp;
    private BigInteger inPermanentBytes;
    private BigInteger inPermanentPkts;
}
