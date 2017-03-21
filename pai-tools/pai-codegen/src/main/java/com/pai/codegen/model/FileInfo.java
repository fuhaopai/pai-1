package com.pai.codegen.model;

import com.pai.codegen.constants.ImageType;
import com.pai.codegen.util.DateConverter;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileInfo
{
  private String fileName = "";
  private String filePath = "";
  private String lastModifiedTime = "";
  private boolean isHidden = false;
  private boolean isDir = false;
  private boolean hasFile = false;
  private long fileSize = 0L;
  private String fileSizeDesc = "";
  private boolean isPhoto = false;
  private String fileType = "";

  public FileInfo(File file) {
    this.fileName = file.getName();
    this.lastModifiedTime = DateConverter.toString(file.lastModified());
    this.filePath = file.getAbsolutePath().replaceAll("[////]", "/");
    this.isHidden = file.isHidden();

    if (file.isDirectory()) {
      this.isDir = true;
      this.hasFile = (file.listFiles() != null);
      this.fileSize = 0L;
      this.fileSizeDesc = "0byte";
      this.isPhoto = false;
      this.fileType = "DIRECTORY";
    } else if (file.isFile()) {
      this.isDir = false;
      this.hasFile = false;
      this.fileSize = file.length();
      if (this.fileSize / 1024L / 1024L / 1024L > 0L)
        this.fileSizeDesc = (this.fileSize / 1024L / 1024L / 1024L + "G");
      else if (this.fileSize / 1024L / 1024L > 0L)
        this.fileSizeDesc = (this.fileSize / 1024L / 1024L + "M");
      else if (this.fileSize / 1024L > 0L)
        this.fileSizeDesc = (this.fileSize / 1024L + "K");
      else {
        this.fileSizeDesc = (this.fileSize + "byte");
      }
      String fileExt = this.fileName.substring(this.fileName.lastIndexOf(".") + 1).toLowerCase();
      this.isPhoto = Arrays.asList(ImageType.toArray()).contains(fileExt);
      this.fileType = fileExt;
    }
  }

  public String getFileName() {
    return this.fileName;
  }
  public String getFilePath() {
    return this.filePath;
  }
  public String getLastModifiedTime() {
    return this.lastModifiedTime;
  }
  public boolean isHidden() {
    return this.isHidden;
  }
  public boolean isDir() {
    return this.isDir;
  }
  public boolean isHasFile() {
    return this.hasFile;
  }
  public long getFileSize() {
    return this.fileSize;
  }
  public String getFileSizeDesc() {
    return this.fileSizeDesc;
  }
  public boolean isPhoto() {
    return this.isPhoto;
  }
  public String getFileType() {
    return this.fileType;
  }
}