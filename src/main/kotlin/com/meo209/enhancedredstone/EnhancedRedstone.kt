package com.meo209.enhancedredstone

import net.fabricmc.api.ModInitializer
import net.minecraft.block.DispenserBlock
import net.minecraft.item.Items

class EnhancedRedstone : ModInitializer {

    override fun onInitialize() {
        DispenserBlock.registerBehavior(Items.BUCKET, MilkDispenserBehavior())
    }
}
