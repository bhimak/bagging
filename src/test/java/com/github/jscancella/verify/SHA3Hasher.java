package com.github.jscancella.verify;

import java.io.IOException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.github.jscancella.hash.Hasher;
import com.github.jscancella.hash.internal.MessageDigestHasher;

public enum SHA3Hasher implements Hasher {
  INSTANCE;// using enum to enforce singleton
  private static final String MESSAGE_DIGEST_NAME = "SHA3-256";
  private MessageDigest messageDigestInstance;

  @Override
  public String hash(Path path) throws IOException, NoSuchAlgorithmException{
    final MessageDigest messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
    MessageDigestHasher.updateMessageDigest(path, messageDigest);
    return MessageDigestHasher.formatMessageDigest(messageDigest);
  }

  @Override
  public void update(byte[] bytes, int length) throws NoSuchAlgorithmException{
    if(messageDigestInstance == null) {
      messageDigestInstance = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
    }
    messageDigestInstance.update(bytes, 0, length);
  }

  @Override
  public String getHash() throws NoSuchAlgorithmException{
    if(messageDigestInstance == null) {
      messageDigestInstance = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
    }
    return MessageDigestHasher.formatMessageDigest(messageDigestInstance);
  }

  @Override
  public void reset(){
    messageDigestInstance = null;
  }

  @Override
  public String getBagitAlgorithmName(){
    return "sha3256";
  }

}