/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.extent.clipboard;

import com.boydti.fawe.beta.Filter;
import com.boydti.fawe.config.Settings;
import com.boydti.fawe.object.clipboard.CPUOptimizedClipboard;
import com.boydti.fawe.object.clipboard.DiskOptimizedClipboard;
import com.boydti.fawe.object.clipboard.MemoryOptimizedClipboard;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.Regions;
import com.sk89q.worldedit.util.Location;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.net.URI;
import java.util.Iterator;
import java.util.UUID;

/**
 * Specifies an object that implements something suitable as a "clipboard."
 */
public interface Clipboard extends Extent, Iterable<BlockVector3>, Closeable {
    static Clipboard create(BlockVector3 size, UUID uuid) {
        if (Settings.IMP.CLIPBOARD.USE_DISK) {
            return new DiskOptimizedClipboard(size, uuid);
        } else if (Settings.IMP.CLIPBOARD.COMPRESSION_LEVEL == 0) {
            return new CPUOptimizedClipboard(size);
        } else {
            return new MemoryOptimizedClipboard(size);
        }
    }

    /**
     * Get the bounding region of this extent.
     *
     * <p>Implementations should return a copy of the region.</p>
     *
     * @return the bounding region
     */
    Region getRegion();

    /**
     * Get the dimensions of the copy, which is at minimum (1, 1, 1).
     *
     * @return the dimensions
     */
    BlockVector3 getDimensions();

    /**
     * Get the origin point from which the copy was made from.
     *
     * @return the origin
     */
    BlockVector3 getOrigin();

    /**
     * Set the origin point from which the copy was made from.
     *
     * @param origin the origin
     */
    void setOrigin(BlockVector3 origin);

    /**
     * Returns true if the clipboard has biome data. This can be checked since {@link Extent#getBiome(BlockVector2)}
     * strongly suggests returning {@link com.sk89q.worldedit.world.biome.BiomeTypes#OCEAN} instead of {@code null}
     * if biomes aren't present. However, it might not be desired to set areas to ocean if the clipboard is defaulting
     * to ocean, instead of having biomes explicitly set.
     *
     * @return true if the clipboard has biome data set
     */
    default boolean hasBiomes() {
        return false;
    }

    /**
     * Remove entity from clipboard
     * @param entity
     */
    void removeEntity(Entity entity);

    default int getWidth() {
        return getDimensions().getBlockX();
    }

    default int getHeight() {
        return getDimensions().getBlockY();
    }

    default int getLength() {
        return getDimensions().getBlockZ();
    }

    default int getArea() {
        return getWidth() * getLength();
    }

    default int getVolume() {
        return getWidth() * getHeight() * getLength();
    }

    default Iterator<BlockVector3> iterator() {
        return getRegion().iterator();
    }

    default Iterator<BlockVector2> iterator2d() {
        return Regions.asFlatRegion(getRegion()).asFlatRegion().iterator();
    }

    default URI getURI() {
        return null;
    }

//    default void paste(Extent other, BlockVector3 to) {
//      TODO FIXME
//    }

    @Override
    default <T extends Filter> T apply(Region region, T filter) {
        if (region.equals(getRegion())) {
            return apply(this, filter);
        } else {
            return apply((Iterable<BlockVector3>) region, filter);
        }
    }

    @Override
    default void close() {

    }
}
