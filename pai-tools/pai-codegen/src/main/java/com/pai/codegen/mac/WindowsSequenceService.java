package com.pai.codegen.mac;

import com.pai.codegen.util.EncryptUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintStream;

public final class WindowsSequenceService extends AbstractSequenceService
{
  private static String BASE_KEY = "PAiCOdeGEN";

  public void execute() { String cpuID = getCPUSerial();
    String machineCode = EncryptUtil.md5(new StringBuilder().append(cpuID).append(BASE_KEY).toString());
    System.out.println(machineCode);
  }

  private String getCPUSerial()
  {
    StringBuilder result = new StringBuilder();
    try {
      File file = File.createTempFile("tmp", ".vbs");
      file.deleteOnExit();
      FileWriter fw = new FileWriter(file); 
      try { 
    	  String vbs = "On Error Resume Next \r\n\r\nstrComputer = \".\"  \r\nSet objWMIService = GetObject(\"winmgmts:\" _ \r\n    & \"{impersonationLevel=impersonate}!\\\\\" & strComputer & \"\\root\\cimv2\") \r\nSet colItems = objWMIService.ExecQuery(\"Select * from Win32_Processor\")  \r\n For Each objItem in colItems\r\n     Wscript.Echo objItem.ProcessorId  \r\n     exit for  ' do the first cpu only! \r\nNext                    ";
        fw.write(vbs);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
      finally
      {
        if (fw != null) 
        	try {
        		fw.close(); 
        		}
        catch (Exception ex) {
        	 ex.printStackTrace();
        	} 
      }
      Process p = Runtime.getRuntime().exec(new StringBuilder().append("cscript //NoLogo ").append(file.getPath()).toString());
      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream())); 
      try
      {
        String line;
        while ((line = input.readLine()) != null)
          result.append(line);
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
      finally
      {
        if (input != null) 
        	try { input.close(); } 
        catch (Exception ex) 
        	{ex.printStackTrace();} 
      }
      file.delete();
    }
    catch (Throwable e) {
    }
    if (result.length() < 1);
    return result.toString();
  }

  public static void main(String[] args) {
    WindowsSequenceService s = new WindowsSequenceService();
    s.execute();
  }
}