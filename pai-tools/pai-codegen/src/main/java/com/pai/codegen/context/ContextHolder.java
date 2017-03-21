 package com.pai.codegen.context;
 
 import com.pai.codegen.util.StringUtils;
 import java.io.File;
 import org.springframework.util.Log4jConfigurer;
 
 /**
  *  配置文件位置的上下文
  *  把配置文件所在的路径存储至本地线程中，并初始化日志接口。
  *  所有的配置文件设置在rootPath+“/conf/”目录
  * @author Administrator
  *
  */
 public class ContextHolder
 {
   private static final ThreadLocal<ContextInfo> holder = new ThreadLocal<ContextInfo>();
 
   public static void setRootPath(String rootPath_) { 
		   init();
	     String rootPath = rootPath_;
	     if (StringUtils.isEmpty(rootPath)) {
	       rootPath = new File("").getAbsolutePath();
	     }
	     if (!rootPath.endsWith("\\"))
	       rootPath = rootPath + "\\";
	     ((ContextInfo)holder.get()).setRootPath(rootPath);
	 
	     initLog4j(rootPath); 
     }
 
   public static String getRootPath()
   {
     init();
     return ((ContextInfo)holder.get()).getRootPath();
   }
 
   private static void init() {
     if (holder.get() == null) {
       ContextInfo contextInfo = new ContextInfo();
       holder.set(contextInfo);
     }
   }
 
   private static void initLog4j(String rootPath) {
     try { 
    	 Log4jConfigurer.initLogging(rootPath + "/conf/log4j.properties");
     } catch (Exception e) {
      e.printStackTrace();
     }
   }
 }