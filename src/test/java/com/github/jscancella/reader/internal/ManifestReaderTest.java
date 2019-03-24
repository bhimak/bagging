package com.github.jscancella.reader.internal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import com.github.jscancella.TempFolderTest;
import com.github.jscancella.domain.Bag;
import com.github.jscancella.exceptions.InvalidBagitFileFormatException;
import com.github.jscancella.exceptions.MaliciousPathException;

public class ManifestReaderTest extends TempFolderTest {
  
  @Test
  public void testReadAllManifests() throws Exception{
    Path rootBag = Paths.get(getClass().getClassLoader().getResource("bags/v0_97/bag").toURI());
    
    Bag.Builder bagBuilder = new Bag.Builder().rootDirectory(rootBag);
    ManifestReader.readAllManifests(rootBag, bagBuilder);
    Assertions.assertEquals(1, bagBuilder.build().getPayLoadManifests().size());
    Assertions.assertEquals(1, bagBuilder.build().getTagManifests().size());
  }
  
  @Test
  public void testReadUpDirectoryMaliciousManifestThrowsException() throws Exception{
    Path manifestFile = Paths.get(getClass().getClassLoader().getResource("maliciousManifestFile/upAdirectoryReference.txt").toURI());
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> { ManifestReader.readChecksumFileMap(manifestFile, Paths.get("/foo"), StandardCharsets.UTF_8); });
  }
  
  @Test
  public void testReadTildeMaliciousManifestThrowsException() throws Exception{
    Path manifestFile = Paths.get(getClass().getClassLoader().getResource("maliciousManifestFile/tildeReference.txt").toURI());
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> { ManifestReader.readChecksumFileMap(manifestFile, Paths.get("/foo"), StandardCharsets.UTF_8); });
  }
  
  @Test
  @EnabledOnOs(OS.WINDOWS)
  public void testReadFileUrlMaliciousManifestThrowsException() throws Exception{
    Path manifestFile = Paths.get(getClass().getClassLoader().getResource("maliciousManifestFile/fileUrl.txt").toURI());
    Assertions.assertThrows(MaliciousPathException.class, 
        () -> {  ManifestReader.readChecksumFileMap(manifestFile, Paths.get("/bar"), StandardCharsets.UTF_8); });
  }
  
  @Test
  public void testReadWindowsSpecialDirMaliciousManifestThrowsException() throws Exception{
    Path manifestFile = Paths.get(getClass().getClassLoader().getResource("maliciousManifestFile/windowsSpecialDirectoryName.txt").toURI());
    Assertions.assertThrows(InvalidBagitFileFormatException.class, 
        () -> { ManifestReader.readChecksumFileMap(manifestFile, Paths.get("/foo"), StandardCharsets.UTF_8); });
  }
}
