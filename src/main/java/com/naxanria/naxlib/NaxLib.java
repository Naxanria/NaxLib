package com.naxanria.naxlib;

import com.naxanria.naxlib.client.event.ClientEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NaxLib.MODID)
public class NaxLib
{
  public static final String MODID = "naxlib";
  
  public NaxLib()
  {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    
    DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
    {
//      modEventBus.addListener(ClientEventHandler::clientTick);
    }
    );
  }
}
