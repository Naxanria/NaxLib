package com.naxanria.naxlib.client.event;

import com.naxanria.naxlib.NaxLib;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = NaxLib.MODID)
public final class ClientEventHandler
{
  @SubscribeEvent
  public static void clientTick(final TickEvent.ClientTickEvent event)
  {
    if (event.phase == TickEvent.Phase.START)
    {
      KeyHandler.update();
    }
  }
}
