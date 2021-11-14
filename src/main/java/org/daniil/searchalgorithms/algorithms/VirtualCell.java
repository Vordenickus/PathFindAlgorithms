package org.daniil.searchalgorithms.algorithms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VirtualCell {

    private final int x, y;
    private boolean visited;

}
