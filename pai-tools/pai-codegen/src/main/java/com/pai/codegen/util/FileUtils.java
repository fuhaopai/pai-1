package com.pai.codegen.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pai.codegen.model.FileInfo;

public class FileUtils
{
  private static Log log = LogFactory.getLog(FileUtils.class);

  public static void createDir(String dir, boolean ignoreIfExitst)
    throws IOException
  {
    File file = new File(dir);
    if ((ignoreIfExitst) && (file.exists())) {
      return;
    }
    if (!file.mkdir())
      throw new IOException("Cannot create the directory = " + dir);
  }

  public static void createDirs(String dir, boolean ignoreIfExitst)
    throws IOException
  {
    File file = new File(dir);
    if ((ignoreIfExitst) && (file.exists())) {
      return;
    }
    if (!file.mkdirs())
      throw new IOException("Cannot create directories = " + dir);
  }

  public static void deleteFile(String filename)
    throws IOException
  {
    File file = new File(filename);
    log.trace("Delete file = " + filename);
    if (file.isDirectory()) {
      throw new IOException("IOException -> BadInputException: not a file.");
    }

    if (!file.exists()) {
      throw new IOException("IOException -> BadInputException: file is not exist.");
    }

    if (!file.delete())
      throw new IOException("Cannot delete file. filename = " + filename);
  }

  public static void deleteDir(File dir)
    throws IOException
  {
    if (dir.isFile()) {
      throw new IOException("IOException -> BadInputException: not a directory.");
    }
    File[] files = dir.listFiles();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        File file = files[i];
        if (file.isFile())
          file.delete();
        else {
          deleteDir(file);
        }
      }
    }
    dir.delete();
  }

  public static String getPathSeparator() {
    return File.pathSeparator;
  }

  public static String getFileSeparator() {
    return File.separator;
  }

  public static List<FileInfo> getFileInfos(String path)
    throws IOException
  {
    File dir = new File(path);
    if (dir.isFile())
      throw new IOException("BadInputException: not a directory.");
    if (!dir.exists()) {
      throw new IOException(" don't exist ");
    }
    File[] files = dir.listFiles();
    int LEN = 0;
    if (files != null) {
      LEN = files.length;
    }
    List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
    for (int i = 0; i < LEN; i++) {
      FileInfo fileInfo = new FileInfo(files[i]);
      fileInfoList.add(fileInfo);
    }
    return fileInfoList;
  }

  public static List<File> getFiles(String path, String suffix, boolean isDepth)
  {
    File dirOrFile = new File(path);
    return getFiles(dirOrFile, suffix, isDepth);
  }

  public static List<File> getFiles(File dirOrFile, String suffix, boolean isDepth)
  {
    List<File> results = new ArrayList<File>();
    listFile(results, dirOrFile, suffix, isDepth);
    return results;
  }

  public static List<String> getFilePaths(String path, String suffix, boolean isDepth)
  {
    List<File> files = getFiles(path, suffix, isDepth);
    List<String> filePaths = new ArrayList<String>();
    for (File file : files) {
      filePaths.add(file.getAbsolutePath());
    }
    return filePaths;
  }

  private static void listFile(List<File> results, File dirOrFile, String suffix, boolean isDepth)
  {
    if ((dirOrFile.isDirectory()) && (isDepth == true)) {
      File[] t = dirOrFile.listFiles();
      for (int i = 0; i < t.length; i++)
        listFile(results, t[i], suffix, isDepth);
    }
    else {
      String filePath = dirOrFile.getAbsolutePath();
      if (dirOrFile.isFile())
        if (StringUtils.isNotEmpty(suffix)) {
          int begIndex = filePath.lastIndexOf(".");
          String tempsuffix = "";
          if (begIndex != -1) {
            tempsuffix = filePath.substring(begIndex + 1, filePath.length());
          }

          if (tempsuffix.equals(suffix))
            results.add(dirOrFile);
        }
        else
        {
          results.add(dirOrFile);
        }
    }
  }

  public static long getDirLength(File dir)
    throws IOException
  {
    if (dir.isFile())
      throw new IOException("BadInputException: not a directory.");
    long size = 0L;
    File[] files = dir.listFiles();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        File file = files[i];

        long length = 0L;
        if (file.isFile())
          length = file.length();
        else {
          length = getDirLength(file);
        }
        size += length;
      }
    }
    return size;
  }

  public static void emptyFile(String srcFilename)
    throws IOException
  {
    File srcFile = new File(srcFilename);
    if (!srcFile.exists()) {
      throw new FileNotFoundException("Cannot find the file: " + srcFile.getAbsolutePath());
    }

    if (!srcFile.canWrite()) {
      throw new IOException("Cannot write the file: " + srcFile.getAbsolutePath());
    }

    FileOutputStream outputStream = new FileOutputStream(srcFilename);
    outputStream.close();
  }

  public static void writeFile(String content, String fileName, String destEncoding)
    throws FileNotFoundException, IOException
  {
    File file = null;
    try {
      file = new File(fileName);
      if ((!file.exists()) && 
        (!file.createNewFile())) {
        throw new IOException("create file '" + fileName + "' failure.");
      }

      if (!file.isFile()) {
        throw new IOException("'" + fileName + "' is not a file.");
      }
      if (!file.canWrite())
        throw new IOException("'" + fileName + "' is a read-only file.");
    }
    finally
    {
    }
    BufferedWriter out = null;
    try {
      FileOutputStream fos = new FileOutputStream(fileName);
      out = new BufferedWriter(new OutputStreamWriter(fos, destEncoding));
      out.write(content);
      out.flush();
    } catch (FileNotFoundException fe) {
      log.error("Error", fe);
      throw fe;
    } catch (IOException e) {
      log.error("Error", e);
      throw e;
    } finally {
      try {
        if (out != null)
          out.close();
      }
      catch (IOException ex)
      {
      }
    }
  }

  public static void writeFile(String content, File file, String charset)
    throws IOException
  {
    Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset);

    writer.write(content);
    writer.close();
  }

  public static void write(InputStream ins, File file)
  {
    try
    {
      OutputStream os = new FileOutputStream(file);
      int bytesRead = 0;
      byte[] buffer = new byte[8192];
      while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
        os.write(buffer, 0, bytesRead);
      }
      os.close();
      ins.close();
    }
    catch (Exception e)
    {
    }
  }

  public static String readFile(String fileName, String srcEncoding)
    throws FileNotFoundException, IOException
  {
    File file = null;
    try {
      file = new File(fileName);
      if (!file.isFile())
        throw new IOException("'" + fileName + "' is not a file.");
    }
    finally
    {
    }
    BufferedReader reader = null;
    try { StringBuffer result = new StringBuffer(1024);
      FileInputStream fis = new FileInputStream(fileName);
      reader = new BufferedReader(new InputStreamReader(fis, srcEncoding));
      char[] block = new char[512];
      int readLength;
      while (true) { readLength = reader.read(block);
        if (readLength == -1)
          break;
        result.append(block, 0, readLength);
      }
      return result.toString();
    } catch (FileNotFoundException fe) {
      log.error("Error", fe);
      throw fe;
    } catch (IOException e) {
      log.error("Error", e);
      throw e;
    } finally {
      try {
        if (reader != null)
          reader.close();
      }
      catch (IOException ex)
      {
      }
    }
  }

  public static List<String> getLastLines(File file, int fromLine)
    throws IOException, FileNotFoundException
  {
    List<String> lines = new ArrayList<String>();
    BufferedReader br = new BufferedReader(new FileReader(file));
    int lineCount = 0;
    String lineString = "";
    while (null != (lineString = br.readLine())) {
      lineCount++;
      if (fromLine <= lineCount) {
        lines.add(lineString);
      }
    }
    br.close();
    return lines;
  }

  public static void copyFile(String srcFilename, String destFilename, boolean overwrite)
    throws IOException
  {
    File srcFile = new File(srcFilename);

    if (!srcFile.exists()) {
      throw new FileNotFoundException("Cannot find the source file: " + srcFile.getAbsolutePath());
    }

    if (!srcFile.canRead()) {
      throw new IOException("Cannot read the source file: " + srcFile.getAbsolutePath());
    }

    File destFile = new File(destFilename);
    if (!overwrite)
    {
      if (!destFile.exists());
    }
    else if (destFile.exists()) {
      if (!destFile.canWrite()) {
        throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
      }

    }
    else if (!destFile.createNewFile()) {
      throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
    }

    BufferedInputStream inputStream = null;
    BufferedOutputStream outputStream = null;
    byte[] block = new byte[1024];
    try {
      inputStream = new BufferedInputStream(new FileInputStream(srcFile));
      outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
      while (true)
      {
        int readLength = inputStream.read(block);
        if (readLength == -1)
          break;
        outputStream.write(block, 0, readLength);
      }
    } finally {
      if (inputStream != null)
        try {
          inputStream.close();
        }
        catch (IOException ex)
        {
        }
      if (outputStream != null)
        try {
          outputStream.close();
        }
        catch (IOException ex)
        {
        }
    }
  }

  public static void copyFile(File srcFile, File destFile, boolean overwrite)
    throws IOException
  {
    if (!srcFile.exists()) {
      throw new FileNotFoundException("Cannot find the source file: " + srcFile.getAbsolutePath());
    }

    if (!srcFile.canRead()) {
      throw new IOException("Cannot read the source file: " + srcFile.getAbsolutePath());
    }

    if (!overwrite)
    {
      if (!destFile.exists());
    }
    else if (destFile.exists()) {
      if (!destFile.canWrite()) {
        throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
      }

    }
    else if (!destFile.createNewFile()) {
      throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
    }

    BufferedInputStream inputStream = null;
    BufferedOutputStream outputStream = null;
    byte[] block = new byte[1024];
    try {
      inputStream = new BufferedInputStream(new FileInputStream(srcFile));
      outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
      while (true)
      {
        int readLength = inputStream.read(block);
        if (readLength == -1)
          break;
        outputStream.write(block, 0, readLength);
      }
    } finally {
      if (inputStream != null)
        try {
          inputStream.close();
        }
        catch (IOException ex)
        {
        }
      if (outputStream != null)
        try {
          outputStream.close();
        }
        catch (IOException ex)
        {
        }
    }
  }

  public static void copyFiles(String srcDirName, String destDirName, boolean overwrite)
    throws IOException
  {
    File srcDir = new File(srcDirName);

    if (!srcDir.exists()) {
      throw new FileNotFoundException("Cannot find the source directory: " + srcDir.getAbsolutePath());
    }

    File destDir = new File(destDirName);
    if (!overwrite) {
      if (!destDir.exists())
      {
        if (!destDir.mkdirs()) {
          throw new IOException("Cannot create the destination directories = " + destDir);
        }

      }

    }
    else if (!destDir.exists())
    {
      if (!destDir.mkdirs()) {
        throw new IOException("Cannot create the destination directories = " + destDir);
      }

    }

    File[] srcFiles = srcDir.listFiles();
    if ((srcFiles == null) || (srcFiles.length < 1))
    {
      return;
    }

    int SRCLEN = srcFiles.length;
    for (int i = 0; i < SRCLEN; i++)
    {
      File destFile = new File(destDirName + File.separator + srcFiles[i].getName());

      if (srcFiles[i].isFile()) {
        copyFile(srcFiles[i], destFile, overwrite);
      }
      else
        copyFiles(srcFiles[i].getAbsolutePath(), destDirName + File.separator + srcFiles[i].getName(), overwrite);
    }
  }

  public static void zipFile(String srcFilename, String destFilename, boolean overwrite)
    throws IOException
  {
    File srcFile = new File(srcFilename);

    if (!srcFile.exists()) {
      throw new FileNotFoundException("Cannot find the source file: " + srcFile.getAbsolutePath());
    }

    if (!srcFile.canRead()) {
      throw new IOException("Cannot read the source file: " + srcFile.getAbsolutePath());
    }

    if ((destFilename == null) || (destFilename.trim().equals("")))
      destFilename = srcFilename + ".zip";
    else {
      destFilename = destFilename + ".zip";
    }
    File destFile = new File(destFilename);
    if (!overwrite)
    {
      if (!destFile.exists());
    }
    else if (destFile.exists()) {
      if (!destFile.canWrite()) {
        throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
      }

    }
    else if (!destFile.createNewFile()) {
      throw new IOException("Cannot write the destination file: " + destFile.getAbsolutePath());
    }

    BufferedInputStream inputStream = null;
    BufferedOutputStream outputStream = null;
    ZipOutputStream zipOutputStream = null;
    byte[] block = new byte[1024];
    try {
      inputStream = new BufferedInputStream(new FileInputStream(srcFile));
      outputStream = new BufferedOutputStream(new FileOutputStream(destFile));

      zipOutputStream = new ZipOutputStream(outputStream);

      zipOutputStream.setComment("通过java程序压缩的");
      ZipEntry zipEntry = new ZipEntry(srcFile.getName());
      zipEntry.setComment(" zipEntry通过java程序压缩的");
      zipOutputStream.putNextEntry(zipEntry);
      while (true) {
        int readLength = inputStream.read(block);
        if (readLength == -1)
          break;
        zipOutputStream.write(block, 0, readLength);
      }
      zipOutputStream.flush();
      zipOutputStream.finish();
    } finally {
      if (inputStream != null)
        try {
          inputStream.close();
        }
        catch (IOException ex)
        {
        }
      if (outputStream != null)
        try {
          outputStream.close();
        }
        catch (IOException ex)
        {
        }
      if (zipOutputStream != null)
        try {
          zipOutputStream.close();
        }
        catch (IOException ex)
        {
        }
    }
  }

  public static boolean isExistFile(String dir) {
    boolean isExist = false;
    File fileDir = new File(dir);
    if (fileDir.isDirectory()) {
      File[] files = fileDir.listFiles();
      if ((files != null) && (files.length != 0)) {
        isExist = true;
      }
    }
    return isExist;
  }

  public static File[] getFiles(String path)
  {
    File file = new File(path);
    return file.listFiles();
  }

  public static String getCharset(File file) {
    String charset = "GBK";
    byte[] first3Bytes = new byte[3];
    try {
      boolean checked = false;
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

      bis.mark(0);
      int read = bis.read(first3Bytes, 0, 3);
      if (read == -1)
        return charset;
      if ((first3Bytes[0] == -1) && (first3Bytes[1] == -2)) {
        charset = "UTF-16LE";
        checked = true;
      } else if ((first3Bytes[0] == -2) && (first3Bytes[1] == -1))
      {
        charset = "UTF-16BE";
        checked = true;
      } else if ((first3Bytes[0] == -17) && (first3Bytes[1] == -69) && (first3Bytes[2] == -65))
      {
        charset = "UTF-8";
        checked = true;
      }
      bis.reset();

      if (!checked) {
        int loc = 0;
        while ((read = bis.read()) != -1) {
          loc++;
          if (read < 240)
          {
            if ((128 > read) || (read > 191))
            {
              if ((192 <= read) && (read <= 223)) {
                read = bis.read();
                if ((128 > read) || (read > 191))
                {
                  break;
                }

              }
              else if ((224 <= read) && (read <= 239)) {
                read = bis.read();
                if ((128 <= read) && (read <= 191)) {
                  read = bis.read();
                  if ((128 <= read) && (read <= 191)) {
                    charset = "UTF-8";
                  }
                }
              }
            }
          }
        }

      }

      bis.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return charset;
  }

  public static InputStream getInputStream(String filepath) {
    File file = new File(filepath);
    String charset = getCharset(file);
    ByteArrayInputStream stream = null;
    try {
      String str = readFile(filepath, charset);
      stream = new ByteArrayInputStream(str.getBytes(charset));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return stream;
  }

  public static byte[] getBytes(String filePath) {
    File file = new File(filePath);
    return getBytes(file);
  }

  public static byte[] getBytes(File file) {
    byte[] buffer = null;
    try {
      FileInputStream fis = new FileInputStream(file);
      ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
      byte[] b = new byte[1000];
      int n;
      while ((n = fis.read(b)) != -1) {
        bos.write(b, 0, n);
      }
      fis.close();
      bos.close();
      buffer = bos.toByteArray();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return buffer;
  }

  public static void toFile(byte[] bfile, String filePath, String fileName) {
    BufferedOutputStream bos = null;
    FileOutputStream fos = null;
    File file = null;
    try {
      File dir = new File(filePath);
      if ((!dir.exists()) && (dir.isDirectory())) {
        dir.mkdirs();
      }
      file = new File(filePath + "\\" + fileName);
      fos = new FileOutputStream(file);
      bos = new BufferedOutputStream(fos);
      bos.write(bfile);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (bos != null) {
        try {
          bos.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
      if (fos != null)
        try {
          fos.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
    }
  }

  public static void main(String[] args)
    throws IOException
  {
    zipFile("E:/fix/log/NT_debug.log", null, true);
    System.out.println(getFileSeparator());
    List<String> temp = getLastLines(new File("F:/docs/abc.txt"), 6);

    for (String str : temp)
      System.out.println(str);
  }
}