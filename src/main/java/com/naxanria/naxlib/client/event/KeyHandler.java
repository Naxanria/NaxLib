package com.naxanria.naxlib.client.event;



import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.ArrayList;
import java.util.List;

public enum KeyHandler
{
  INSTANCE;

  private List<KeyParser> parsers = new ArrayList<>();

  KeyHandler()
  {}

  public static void register(KeyParser parser)
  {
    ClientRegistry.registerKeyBinding(parser.keyBinding);
    INSTANCE.parsers.add(parser);
  }

  public static void update()
  {
    for (KeyParser kp :
      INSTANCE.parsers)
    {
      kp.update();
    }
  }
}
