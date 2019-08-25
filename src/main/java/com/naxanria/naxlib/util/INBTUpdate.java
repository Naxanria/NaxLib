package com.naxanria.naxlib.util;

import net.minecraft.nbt.CompoundNBT;

public interface INBTUpdate
{
  CompoundNBT writeToNBT(CompoundNBT compound);
  void readFromNBT(CompoundNBT tag);
}
