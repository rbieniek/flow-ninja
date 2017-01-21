package org.flowninja.common.types;

import org.flowninja.common.protocol.types.IPTypeOfService;

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
public class TypeOfService {

    private IPTypeOfService srcTos;
    private IPTypeOfService dstTos;
    private IPTypeOfService PostIpDiffServCodePoint;
}
