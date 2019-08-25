package com.naxanria.naxlib.util;

import net.minecraft.nbt.CompoundNBT;

public interface Serializable<T>
{
  T writeToNBT(CompoundNBT tag);
  
  T readFromNBT(CompoundNBT tag);
}
