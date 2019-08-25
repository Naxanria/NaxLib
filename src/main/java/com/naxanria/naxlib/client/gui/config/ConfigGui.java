package com.naxanria.naxlib.client.gui.config;

import com.naxanria.naxlib.client.gui.ScreenBase;
import com.naxanria.naxlib.util.BiValue;
import com.naxanria.naxlib.util.IntRange;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigGui extends ScreenBase
{
  private List<ConfigGuiEntry<?, ?>> entries = new ArrayList<>();
  private List<String> keys = new ArrayList<>();
  private ForgeConfigSpec spec;
  
  private GuiTooltip tooltip = null;
//  private ConfigGuiEntry<?, ?> lastEntry;
  
  private Map<ConfigCategoryNode, CategoryWidget> widgetMap = new HashMap<>();
  private ConfigCategoryNode categories = ConfigCategoryNode.create();
  private ConfigCategoryNode currentCategory;
  
  private List<String> subCategories;
  
  private CategoryWidget categoryWidget;
  private List<GuiButtonExt> subCategoryButtons = new ArrayList<>();
  private GuiButtonExt saveButton;
  private GuiButtonExt cancelButton;
  
  
  private ConfigGui(Screen parent, Builder builder)
  {
    super(new StringTextComponent("Config"), parent);
  
//    spec = MappyConfig.getSpec();
    
    spec = builder.configSpec;
    
    currentCategory = builder.toTop().currentNode;
    
    String s = I18n.format("naxlib.gui.save");
    int w = font.getStringWidth(s);
    saveButton = new GuiButtonExt(8, 0, w + 8, 20, s, this::save);
    
    s = I18n.format("naxlib.gui.cancel");
    w = font.getStringWidth(s);
    cancelButton = new GuiButtonExt(8 + saveButton.getWidth(), 0, w + 8, 20, s, this::cancel);
    
    setupCategory();
  }
  
  @Override
  public void init()
  {
    windowWidth = minecraft.mainWindow.getScaledWidth();
    windowHeight = minecraft.mainWindow.getScaledHeight();

    children.clear();
  
    int x = 8;
    for (String subCat : subCategories)
    {
      if (subCat.equals("Hidden"))
      {
        continue;
      }
      
      int w = font.getStringWidth(subCat) + 8;
      GuiButtonExt subButton = new GuiButtonExt(x, 22, w, 20, subCat, this::subCat);
      x += w + 1;
      subCategoryButtons.add(subButton);
    }
    
    children.addAll(subCategoryButtons);
    children.addAll(entries);
    
    if (categoryWidget != null)
    {
      children.add(categoryWidget);
    }
    
    saveButton.y = windowHeight - 22 + 2;
    cancelButton.y = saveButton.y;
    
    children.add(saveButton);
    children.add(cancelButton);
  }
  
  private void subCat(Button b)
  {
    setNode(currentCategory.getChild(b.getMessage()));
  }
  
  public void setupCategory()
  {
    entries.clear();
    subCategoryButtons.clear();
    
    subCategories = currentCategory.getChildren();
    entries.addAll(currentCategory.getEntries());
    
    if (!widgetMap.containsKey(currentCategory))
    {
      categoryWidget = new CategoryWidget(8, 2, currentCategory, this);
      widgetMap.put(currentCategory, categoryWidget);
    }
    else
    {
      categoryWidget = widgetMap.get(currentCategory);
    }
    
    init();
  }
//
//  protected ConfigGui addEntry(ForgeConfigSpec.ConfigValue<String> var)
//  {
//    return addEntry(new ConfigGuiEntry<>(spec, var));
//  }
//
//  protected ConfigGui addEntry(ForgeConfigSpec.IntValue var)
//  {
//    return addEntry(new IntegerConfigGuiEntry(spec, var));
//  }
//
//  protected ConfigGui addEntry(ForgeConfigSpec.BooleanValue var)
//  {
//    return addEntry(new BooleanConfigGuiEntry(spec, var));
//  }
//
//  protected <T extends Enum<T>> ConfigGui addEntry(ForgeConfigSpec.EnumValue<T> var)
//  {
//    return addEntry(new EnumConfigGuiEntry<>(spec, var));
//  }
//
//  protected ConfigGui addEntry(ConfigGuiEntry<?, ?> entry)
//  {
//    lastEntry = entry;
//
//    currentCategory.add(entry);
//    tooltip = entry.tooltip;
//
//    return this;
//  }
  
  @Override
  public void renderBackground()
  {
    renderDirtBackground(0);
  }
  
  @Override
  public void render(int mouseX, int mouseY, float partialTicks)
  {
    this.mouseX = mouseX;
    this.mouseY = mouseY;
    
    renderBackground();
    
    renderEntries();
    renderTop();
    renderBottom();
    
    renderForeground();
  }
  
  private void renderEntries()
  {
    int x = 10;
    int y = 48;
    int width = windowWidth - x - 20;
    int scroll = 0;
    int spacing = 2;
  
    int totHeight = 0;
    
    fill(0, 45, windowWidth, windowHeight - 22, 0xaa000000);
  
    tooltip = null;
    
    for (ConfigGuiEntry<?, ?> entry :
      entries)
    {
      entry.width = width;
      entry.setPosition(x, y);
      int h = entry.height;
      totHeight += h + spacing;
    
      y += h + spacing;

      entry.render(mouseX, mouseY, 0);
  
      if (tooltip == null)
      {
        tooltip = entry.getTooltip();
        if (tooltip != null)
        {
          tooltip.x = entry.x;
          tooltip.y = entry.y - tooltip.height - 2;
        }
      }
    }
  }
  
  private void renderBottom()
  {
    int h = 22;
//    fill(0, windowHeight - h, windowWidth, windowHeight, 0x66373737);
    
    saveButton.renderButton(mouseX, mouseY, 0);
    cancelButton.renderButton(mouseX, mouseY, 0);
  }
  
  private void renderTop()
  {
//    fill(0, 0, windowWidth, 45, 0x66373737);
    
    if (categoryWidget != null)
    {
      categoryWidget.render(mouseX, mouseY, 0);
    }
    
    for (GuiButtonExt button : subCategoryButtons)
    {
      button.renderButton(mouseX, mouseY, 0);
    }
  }
  
  @Override
  public void renderPreChildren()
  {
    tooltip = null;
  }
  
  @Override
  public void renderForeground()
  {
    if (tooltip != null)
    {
      tooltip.render(tooltip.x, tooltip.y);
    }
  }
  
  public void setNode(ConfigCategoryNode node)
  {
    this.currentCategory = node;
    setupCategory();
  }
  
  private void save(Button button)
  {
    entries.forEach(ConfigGuiEntry::save);
    
    onClose();
  }
  
  private void cancel(Button button)
  {
    onClose();
  }
  
  public static class Builder
  {
    private final Screen parentScreen;
    private final ForgeConfigSpec configSpec;
    private final ConfigCategoryNode categories = ConfigCategoryNode.create();
    private ConfigCategoryNode currentNode;
    
    private ConfigGuiEntry<?, ?> lastEntry;
    private IntRange range;
    private List<BiValue<String, Integer>> info = new ArrayList<>();
    private String def = null;
  
    private Builder(Screen parentScreen, ForgeConfigSpec configSpec)
    {
      this.parentScreen = parentScreen;
      this.configSpec = configSpec;
      currentNode = categories;
    }
  
    public static Builder create(Screen parenTScreen, ForgeConfigSpec configSpec)
    {
      return new Builder(parenTScreen, configSpec);
    }
    
    public Builder push(String categoryName)
    {
      resolve();
      
      currentNode = currentNode.push(categoryName);
      lastEntry = null;
      
      return this;
    }
  
    public Builder pop()
    {
      return pop(1);
    }
  
    public Builder pop(int amount)
    {
      resolve();
      
      for (int i = 0; i < amount; i++)
      {
        currentNode = currentNode.pop();
      }
      
      return this;
    }
    
    public Builder toTop()
    {
      resolve();
      
      currentNode = currentNode.getTop();
      
      return this;
    }
  
    public Builder add(ForgeConfigSpec.ConfigValue<String> value)
    {
      return add(value, null);
    }
  
    public Builder add(ForgeConfigSpec.ConfigValue<String> value, String defaultValue)
    {
      resolve();
      
      lastEntry = new StringConfigGuiEntry(configSpec, value);
      
      currentNode.add(lastEntry);
      
      def = defaultValue;
      
      return this;
    }
    
    public Builder add(ForgeConfigSpec.IntValue value, int defaultValue, int min, int max)
    {
      resolve();
      
      lastEntry = new IntegerConfigGuiEntry(configSpec, value);
      
      currentNode.add(lastEntry);
      
      range = new IntRange(min, max);
      
      def = defaultValue + "";
      
      return this;
    }
    
    public Builder add(ForgeConfigSpec.BooleanValue value, boolean defaultValue)
    {
      resolve();
      
      lastEntry = new BooleanConfigGuiEntry(configSpec, value);
      
      currentNode.add(lastEntry);
      
      def = defaultValue ? "True" : "False";
      
      return this;
    }
    
    public <T extends Enum<T>> Builder add(ForgeConfigSpec.EnumValue<T> value, T defaultValue)
    {
      resolve();
      
      lastEntry = new EnumConfigGuiEntry<>(configSpec, value);
      
      currentNode.add(lastEntry);
      
      def = defaultValue.toString();
      
      return this;
    }
    
    private void resolve()
    {
      if (lastEntry != null)
      {
        for (BiValue<String, Integer> si:
          info)
        {
          lastEntry.tooltip.addInfo(si.A, si.B);
        }
  
        if (range != null)
        {
          lastEntry.tooltip.range(range.min, range.max);
        }
        
        if (def != null)
        {
          lastEntry.tooltip.def(def);
        }
      }
      
      def = null;
      range = null;
      info.clear();
    }
  
    public Builder comment(String comment)
    {
      return comment(comment, 0xffffffff);
    }
  
    public Builder comment(String comment, int color)
    {
      info.add(new BiValue<>(comment, color));
      
      return this;
    }
    
    public ConfigGui build()
    {
      return new ConfigGui(parentScreen, this);
    }
  }
}
