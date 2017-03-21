package com.pai.codegen.helper.elutil;

import com.pai.codegen.model.config.DirPrefixCollection;
import java.util.Iterator;
import org.dom4j.Element;

public class DirPrefixUtil
{
  public static DirPrefixCollection buildDirPrefixCollection(Element moduleDirs)
  {
    DirPrefixCollection dpc = new DirPrefixCollection();
    for (Iterator j = moduleDirs.elementIterator("dir"); j.hasNext(); ) {
      Element dirEl = (Element)j.next();
      String key = dirEl.attributeValue("key");
      String value = dirEl.attributeValue("value");
      dpc.put(key, value);
    }
    return dpc;
  }
}