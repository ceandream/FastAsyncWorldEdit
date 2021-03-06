package com.boydti.fawe.beta.implementation.filter;

import com.boydti.fawe.beta.Filter;
import com.boydti.fawe.beta.implementation.filter.block.FilterBlock;
import com.sk89q.worldedit.world.block.BlockState;

public class SetFilter implements Filter {

    private final BlockState state;

    public SetFilter(BlockState state) {
        this.state = state;
    }

    @Override
    public void applyBlock(FilterBlock block) {
        block.setBlock(state);
    }
}
