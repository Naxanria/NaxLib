package com.naxanria.naxlib.util;

import net.minecraft.util.math.BlockPos;

public class Util
{
  public static String prettyFy(BlockPos pos)
  {
    return "[" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "]";
  }
}
