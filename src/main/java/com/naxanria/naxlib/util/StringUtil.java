package com.naxanria.naxlib.util;

import java.util.List;

public class StringUtil
{
  public static String reverse(String string)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = string.length() - 1; i >= 0; i--)
    {
      builder.append(string.charAt(i));
    }
    
    return builder.toString();
  }
  
  public static String combine(List<String> path, String joiner)
  {
    StringBuilder combined = new StringBuilder();
    for (int i = 0; i < path.size(); i++)
    {
      combined.append(path.get(i));
      if (i < path.size() - 1)
      {
        combined.append(joiner);
      }
    }
    
    return combined.toString();
  }
  
  public static String compact(List<String> list)
  {
    return compact(list, "\n");
  }
  
  private static String compact(List<String> list, String combine)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < list.size(); i++)
    {
      builder.append(list.get(i));
      if (list.size() - i > 1)
      {
        builder.append(combine);
      }
    }
    
    return builder.toString();
  }
}
