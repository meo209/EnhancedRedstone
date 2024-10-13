package com.meo209.enhancedredstone

import net.minecraft.block.DispenserBlock
import net.minecraft.block.FluidDrainable
import net.minecraft.block.dispenser.ItemDispenserBehavior
import net.minecraft.entity.passive.CowEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.util.math.BlockPointer
import net.minecraft.util.math.Box
import net.minecraft.world.WorldAccess
import net.minecraft.world.event.GameEvent

/**
 * Allows dispensers to milk cows
 */
class MilkDispenserBehavior: ItemDispenserBehavior() {

    override fun dispenseSilently(pointer: BlockPointer, stack: ItemStack): ItemStack {
        val world: WorldAccess = pointer.world()
        val blockPos = pointer.pos().offset(pointer.state()[DispenserBlock.FACING])

        if (stack.isOf(Items.BUCKET)) {
            val cows = world.getEntitiesByClass(CowEntity::class.java, Box(blockPos), EntityPredicates.EXCEPT_SPECTATOR)

            if (cows.isNotEmpty())
                cows.random().also {
                    println("COW")
                    return this.decrementStackWithRemainder(pointer, stack, ItemStack(Items.MILK_BUCKET))
                }
        }

        val blockState = world.getBlockState(blockPos)
        if (blockState.block is FluidDrainable) {
            val fluidDrainable = blockState.block as FluidDrainable
            val itemStack: ItemStack = fluidDrainable.tryDrainFluid(null, world, blockPos, blockState)
            if (!itemStack.isEmpty) {
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos)
                return this.decrementStackWithRemainder(pointer, stack, itemStack)
            }
        }

        // Fallback to default dispenser behavior
        return super.dispenseSilently(pointer, stack)
    }
}
