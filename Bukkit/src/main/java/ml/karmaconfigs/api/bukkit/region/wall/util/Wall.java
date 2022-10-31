package ml.karmaconfigs.api.bukkit.region.wall.util;

/*
 * This file is part of KarmaAPI, licensed under the MIT License.
 *
 *  Copyright (c) karma (KarmaDev) <karmaconfigs@gmail.com>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

import ml.karmaconfigs.api.bukkit.region.Cuboid;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Set;

/**
 * Karma region wall
 */
public abstract class Wall {

    /**
     * Get the wall type
     *
     * @return the wall type
     */
    public abstract WallType getType();

    /**
     * Get the region that owns the walls
     *
     * @return the region
     */
    public abstract Cuboid getRegion();

    /**
     * Get the wall blocks
     *
     * @return the wall blocks
     */
    public abstract Set<Block> getWall();

    /**
     * Get nearby entities in the wall
     *
     * @param range the search radius
     * @return the near wall entities
     */
    public abstract Set<Entity> getNear(final double range);

    /**
     * Get nearby entities in the wall
     *
     * @param range the search radius
     * @param filter the entities to ignore
     * @return the near wall entities
     */
    public abstract Set<Entity> getNear(final double range, final EntityType... filter);
}
