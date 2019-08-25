package com.naxanria.naxlib.client.event;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;

public abstract class KeyParser
{
  public final KeyBinding keyBinding;
  public static final Minecraft mc = Minecraft.getInstance();
  
  private boolean state = false;
  private boolean lastState = false;

  protected KeyParser(KeyBinding keyBinding)
  {
    this.keyBinding = keyBinding;
  }
  
  protected KeyParser(String name, int key, KeyConflictContext conflictContext, String category)
  {
    this(createKeyBinding(name, key, conflictContext, category));
  }
  
  private static KeyBinding createKeyBinding(String name, int key, KeyConflictContext conflictContext, String category)
  {
    return new KeyBinding(name, conflictContext, InputMappings.Type.KEYSYM, key, category);
  }

  final void update()
  {
    lastState = state;
    state = keyBinding.isKeyDown();
    
    if (isListening())
    {
      if (state && !lastState)
      {
        onKeyDown();
      }
      else if (!state && lastState)
      {
        onKeyUp();
      }
    }
  }

  public void onKeyDown()
  {}

  public void onKeyUp()
  {}

  public boolean isListening()
  {
    return true;
  }
}
