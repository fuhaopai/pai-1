package com.pai.codegen.mac;

import com.pai.codegen.util.EncryptUtil;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

public abstract class AbstractSequenceService
{
  protected String getSplitString(String str)
  {
    return getSplitString(str, "-", 4);
  }

  protected String getSplitString(String str, String split, int length)
  {
    int len = str.length();
    StringBuilder temp = new StringBuilder();
    for (int i = 0; i < len; i++) {
      if ((i % length == 0) && (i > 0)) {
        temp.append(split);
      }
      temp.append(str.charAt(i));
    }
    String[] attrs = temp.toString().split(split);
    StringBuilder finalMachineCode = new StringBuilder();
    for (String attr : attrs) {
      if (attr.length() == length) {
        finalMachineCode.append(attr).append(split);
      }
    }
    String result = finalMachineCode.toString().substring(0, finalMachineCode.toString().length() - 1);
    return result;
  }

  protected String getSigarSequence(String osName)
  {
    try
    {
      Set<String> result = new HashSet<String>();
      Properties props = System.getProperties();
      String javaVersion = props.getProperty("java.version");
      result.add(javaVersion);
      String javaVMVersion = props.getProperty("java.vm.version");
      result.add(javaVMVersion);
      String osVersion = props.getProperty("os.version");
      result.add(osVersion);
      Sigar sigar = new Sigar();
      Mem mem = sigar.getMem();

      String totalMem = new StringBuilder().append(mem.getTotal() / 1024L).append("K av").toString();
      result.add(totalMem);

      return EncryptUtil.md5(result.toString());
    }
    catch (Throwable ex)
    {
    }

    return null;
  }
}