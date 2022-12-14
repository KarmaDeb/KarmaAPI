package ml.karmaconfigs.api.bukkit.region.dummy;

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

import ml.karmaconfigs.api.bukkit.KarmaPlugin;
import ml.karmaconfigs.api.bukkit.region.Cuboid;
import ml.karmaconfigs.api.bukkit.region.event.block.*;
import ml.karmaconfigs.api.bukkit.region.flag.FlagState;
import ml.karmaconfigs.api.bukkit.region.flag.RegionFlag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dummy block events listener
 */
public class BlockListener implements Listener {

    private final Set<Integer> parsed = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * Initialize the block listener
     */
    public BlockListener() {
    }

    /**
     * Generic event listener
     *
     * @param e the event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (parsed.contains(e.hashCode())) {
            Block block = e.getBlock();
            Player player = e.getPlayer();

            Cuboid.getRegions().forEach((region) -> {
                if (region.isInside(block)) {
                    Cuboid event_region = region;

                    Cuboid global = region.getGlobal();
                    Cuboid high_child = region;
                    Set<Cuboid> children = region.getInside();
                    for (Cuboid inside : children) {
                        if (inside.getPriority() > high_child.getPriority()) {
                            high_child = inside;
                        }
                    }

                    if (global.getPriority() > high_child.getPriority()) {
                        if (global.isInside(block)) {
                            event_region = global;
                        } else {
                            if (high_child.isInside(block)) {
                                event_region = high_child;
                            }
                        }
                    } else {
                        if (high_child.isInside(block)) {
                            event_region = high_child;
                        } else {
                            if (global.isInside(block)) {
                                event_region = global;
                            }
                        }
                    }

                    BlockModifiedAtRegionEvent event = new BlockModifiedAtRegionEvent(block, player, BlockAction.BUILD, event_region);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        e.setCancelled(false);

                        RegionFlag<FlagState> state = event_region.getUnsafeFlag("build");
                        switch (state.getValue()) {
                            case ALLOW:
                                e.setBuild(true);
                                break;
                            case DENY:
                                e.setBuild(false);
                                break;
                            case DEFAULT:
                            default:
                                e.setBuild(e.canBuild());
                                break;
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            });

            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(KarmaPlugin.getABC(), () -> parsed.remove(e.hashCode()), 20 * 5);
        }
    }

    /**
     * Generic event listener
     *
     * @param e the event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (parsed.contains(e.hashCode())) {
            Block block = e.getBlock();
            Player player = e.getPlayer();

            Cuboid.getRegions().forEach((region) -> {
                if (region.isInside(block)) {
                    Cuboid event_region = region;

                    Cuboid global = region.getGlobal();
                    Cuboid high_child = region;
                    Set<Cuboid> children = region.getInside();
                    for (Cuboid inside : children) {
                        if (inside.getPriority() > high_child.getPriority()) {
                            high_child = inside;
                        }
                    }

                    if (global.getPriority() > high_child.getPriority()) {
                        if (global.isInside(block)) {
                            event_region = global;
                        } else {
                            if (high_child.isInside(block)) {
                                event_region = high_child;
                            }
                        }
                    } else {
                        if (high_child.isInside(block)) {
                            event_region = high_child;
                        } else {
                            if (global.isInside(block)) {
                                event_region = global;
                            }
                        }
                    }

                    BlockModifiedAtRegionEvent event = new BlockModifiedAtRegionEvent(block, player, BlockAction.DESTROY, event_region);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        RegionFlag<FlagState> state = event_region.getUnsafeFlag("break");
                        switch (state.getValue()) {
                            case ALLOW:
                                e.setCancelled(false);
                                break;
                            case DENY:
                                e.setCancelled(true);
                                break;
                            case DEFAULT:
                            default:
                                break;
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            });

            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(KarmaPlugin.getABC(), () -> parsed.remove(e.hashCode()), 20 * 5);
        }
    }

    /**
     * Generic event listener
     *
     * @param e the event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void entityBlock(EntityChangeBlockEvent e) {
        if (parsed.contains(e.hashCode())) {
            Block block = e.getBlock();
            String name = e.getTo().name();
            Entity entity = e.getEntity();

            Cuboid.getRegions().forEach((region) -> {
                if (region.isInside(block)) {
                    Cuboid event_region = region;

                    Cuboid global = region.getGlobal();
                    Cuboid high_child = region;
                    Set<Cuboid> children = region.getInside();
                    for (Cuboid inside : children) {
                        if (inside.getPriority() > high_child.getPriority()) {
                            high_child = inside;
                        }
                    }

                    if (global.getPriority() > high_child.getPriority()) {
                        if (global.isInside(block)) {
                            event_region = global;
                        } else {
                            if (high_child.isInside(block)) {
                                event_region = high_child;
                            }
                        }
                    } else {
                        if (high_child.isInside(block)) {
                            event_region = high_child;
                        } else {
                            if (global.isInside(block)) {
                                event_region = global;
                            }
                        }
                    }

                    BlockAction action = BlockAction.BUILD;

                    if (name.equalsIgnoreCase("air") || name.equalsIgnoreCase("cave_air")) {
                        action = BlockAction.DESTROY;
                    }

                    BlockModifiedAtRegionEvent event = new BlockModifiedAtRegionEvent(block, entity, action, region);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        RegionFlag<FlagState> state = event_region.getUnsafeFlag("entity-block-" + (action.equals(BlockAction.DESTROY) ? "break" : "build"));
                        switch (state.getValue()) {
                            case ALLOW:
                                e.setCancelled(true);
                                break;
                            case DENY:
                                e.setCancelled(false);
                                break;
                            case DEFAULT:
                            default:
                                break;
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            });

            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(KarmaPlugin.getABC(), () -> parsed.remove(e.hashCode()), 20 * 5);
        }
    }

    /**
     * Generic event listener
     *
     * @param e the event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onIgnite(BlockIgniteEvent e) {
        Block block = e.getBlock();
        Entity entity = e.getIgnitingEntity();
        Block source = e.getIgnitingBlock();

        Cuboid.getRegions().forEach((region) -> {
            if (region.isInside(block)) {
                BlockAction action = BlockAction.PROPAGATE;

                BlockFireAtRegionEvent event = new BlockFireAtRegionEvent(block, action, region);
                Bukkit.getServer().getPluginManager().callEvent(event);

                e.setCancelled(event.isCancelled());

                if (!event.isCancelled()) {
                    BlockModifiedAtRegionEvent modified;

                    if (entity instanceof LivingEntity) {
                        modified = new BlockModifiedAtRegionEvent(block, entity, action, region);
                    } else {
                        modified = new BlockModifiedAtRegionEvent(block, source, action, region);
                    }
                    Bukkit.getServer().getPluginManager().callEvent(modified);

                    e.setCancelled(modified.isCancelled());
                }
            }
        });
    }

    /**
     * Generic event listener
     *
     * @param e the event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBurn(BlockBurnEvent e) {
        Block block = e.getBlock();

        Cuboid.getRegions().forEach((region) -> {
            if (region.isInside(block)) {
                BlockAction action = BlockAction.BURN;

                BlockFireAtRegionEvent event = new BlockFireAtRegionEvent(block, action, region);
                Bukkit.getServer().getPluginManager().callEvent(event);

                e.setCancelled(event.isCancelled());

                if (!event.isCancelled()) {
                    BlockModifiedAtRegionEvent modified = new BlockModifiedAtRegionEvent(block, null, action, region);
                    Bukkit.getServer().getPluginManager().callEvent(modified);

                    e.setCancelled(modified.isCancelled());
                }
            }
        });
    }

    /**
     * Generic event listener
     *
     * @param e the event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpread(BlockSpreadEvent e) {
        Block block = e.getBlock();
        Block source = e.getSource();

        Cuboid.getRegions().forEach((region) -> {
            if (region.isInside(block)) {
                BlockAction action = BlockAction.PROPAGATE;

                BlockPropagationAtRegionEvent event = new BlockPropagationAtRegionEvent(block, source, region);
                Bukkit.getServer().getPluginManager().callEvent(event);

                e.setCancelled(event.isCancelled());

                if (!event.isCancelled()) {
                    BlockModifiedAtRegionEvent modified = new BlockModifiedAtRegionEvent(block, source, action, region);
                    Bukkit.getServer().getPluginManager().callEvent(modified);

                    e.setCancelled(modified.isCancelled());
                }
            }
        });
    }

    /**
     * Generic event listener
     *
     * @param e the event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onForm(BlockFormEvent e) {
        Block source = e.getBlock();
        Block block = e.getNewState().getBlock();

        Cuboid.getRegions().forEach((region) -> {
            if (region.isInside(block)) {
                BlockAction action = BlockAction.PROPAGATE;

                BlockPropagationAtRegionEvent event = new BlockPropagationAtRegionEvent(block, source, region);
                Bukkit.getServer().getPluginManager().callEvent(event);

                e.setCancelled(event.isCancelled());

                if (!event.isCancelled()) {
                    BlockModifiedAtRegionEvent modified = new BlockModifiedAtRegionEvent(block, source, action, region);
                    Bukkit.getServer().getPluginManager().callEvent(modified);

                    e.setCancelled(modified.isCancelled());
                }
            }
        });
    }

    /**
     * Generic event listener
     *
     * @param e the event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFromTo(BlockFromToEvent e) {
        Block source = e.getBlock();
        Block block = e.getToBlock();

        Cuboid.getRegions().forEach((region) -> {
            if (region.isInside(block)) {
                BlockAction action = BlockAction.FLOW;

                Cancellable event;
                if (block.getType().equals(Material.DRAGON_EGG)) {
                    event = new DragonEggAtRegionEvent(block, source, region);
                    action = BlockAction.TELEPORT;
                } else {
                    event = new LiquidFlowAtRegionEvent(block, source, region);
                }

                Bukkit.getServer().getPluginManager().callEvent((Event) event);

                e.setCancelled(event.isCancelled());

                if (!event.isCancelled()) {
                    BlockModifiedAtRegionEvent modified = new BlockModifiedAtRegionEvent(block, source, action, region);
                    Bukkit.getServer().getPluginManager().callEvent(modified);

                    e.setCancelled(modified.isCancelled());
                }
            }
        });
    }

    /**
     * Generic event listener
     *
     * @param e the event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplode(BlockExplodeEvent e) {
        Block block = e.getBlock();
        float yield = e.getYield();

        Cuboid.getRegions().forEach((region) -> {
            boolean fired = false;

            if (region.isInside(block)) {
                BlockAction action = BlockAction.EXPLODE;

                BlockExplodeAtRegionEvent event = new BlockExplodeAtRegionEvent(block, yield, region);
                Bukkit.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    e.setCancelled(true);
                } else {
                    BlockModifiedAtRegionEvent modified = new BlockModifiedAtRegionEvent(block, e.blockList(), action, region);
                    Bukkit.getServer().getPluginManager().callEvent(modified);

                    fired = true;

                    if (modified.isCancelled()) {
                        e.setCancelled(true);
                    } else {
                        e.setYield(event.getYield());

                        Set<Block> differences = new HashSet<>(e.blockList());
                        Block[] affected = modified.getAffectedBlocks();
                        if (affected != null) {
                            Arrays.asList(affected).forEach(differences::remove);
                        }

                        e.blockList().removeAll(differences);
                    }
                }
            }

            Set<Block> remove = new HashSet<>();
            for (Block affected : e.blockList()) {
                if (region.isInside(affected)) {
                    BlockAction action = BlockAction.EXPLODE_BREAK;

                    ExplosionBreakAtRegionEvent event = new ExplosionBreakAtRegionEvent(affected, block, region);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        remove.add(affected);
                    } else {
                        if (!fired) {
                            BlockModifiedAtRegionEvent modified = new BlockModifiedAtRegionEvent(block, affected, action, region);
                            Bukkit.getServer().getPluginManager().callEvent(modified);

                            if (modified.isCancelled()) {
                                remove.add(affected);
                            }
                        }
                    }
                }
            }

            e.blockList().removeAll(remove);
        });
    }
}
