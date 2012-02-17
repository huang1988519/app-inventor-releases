// Copyright 2008 Google Inc. All Rights Reserved.

package com.google.appinventor.server.project.utils;

import com.google.appinventor.server.encryption.EncryptionException;
import com.google.appinventor.server.encryption.EncryptionStrategy;
import com.google.appinventor.server.encryption.Encryptor;
import com.google.appinventor.server.storage.StorageIo;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Security related helper functions.
 *
 * @author markf@google.com (Mark Friedman)
 */
public class Security {
  private static final Logger LOG = Logger.getLogger(Security.class.getName());

  // Radix of the encrypted userID/projectID value
  private static final int ENCRYPTED_ID_RADIX = Character.MAX_RADIX;

  // Number of hex-digits for projectID (bits in a long divided by bits in a hex digit)
  private static final int ID_DIGITS = Long.SIZE / 4;

  private static final Encryptor encryptor = EncryptionStrategy.WRITE;

  private Security() {  // COV_NF_LINE
  }  // COV_NF_LINE

  /**
   * Encrypts a user ID and a project ID so that it can be included in an URL
   * without leaking information. The encryption key is the same across all
   * servers so that any server can decrypt a request.
   *
   * @param userId  user ID
   * @param projectId  project ID
   * @return  an encrypted string safe to include in an URL
   */
  public static String encryptUserAndProjectId(String userId, long projectId)
      throws EncryptionException {
    if ((userId == null) || (userId.isEmpty())) {
      throw new EncryptionException("Trying to encrypt a null userId");
    }
    // We encrypt the projectId as a fixed number of digits, followed by
    // the arbitrary length userId.
    String plain = String.format("%1$0" + ID_DIGITS + "x", projectId) + userId;
    BigInteger bigint = new BigInteger(padBytes(encryptor.encrypt(plain.getBytes())));
    return bigint.toString(ENCRYPTED_ID_RADIX);
  }

  /**
   * Decrypt the user ID from an encrypted string generated by
   * {@link #encryptUserAndProjectId(String, long)}.
   *
   * @param idEnc  string generated by encryptUserAndProjectId
   * @return  the userId parameter that was originally passed to
   *          encryptUserAndProjectId or null
   *          if the encrypted string was invalid
   */
  public static String decryptUserId(String idEnc) {
    try {
      // Decrypt, skip the projectId (fixed length) and return the
      // rest of the decrypted string as the userId
      BigInteger bigint = new BigInteger(idEnc, ENCRYPTED_ID_RADIX);
      String decryptedString = new String(encryptor.decrypt(unpadBytes(bigint.toByteArray())));
      return decryptedString.substring(ID_DIGITS);
    } catch (EncryptionException e) {  // COV_NF_LINE
      LOG.log(Level.SEVERE, "Decryption failed with error", e);
      return null;  // COV_NF_LINE
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Decrypt the project ID from an encrypted string generated by
   * {@link #encryptUserAndProjectId(String, long)}.
   *
   * @param idEnc  string generated by encryptUserAndProjectId
   * @return  the projectId parameter that was originally passed to
   *          encryptUserAndProjectId or
   *          {@link StorageIo#INVALID_PROJECTID} if the encrypted string
   *          was invalid
   */
  public static long decryptProjectId(String idEnc) {
    try {
      BigInteger bigint = new BigInteger(idEnc, ENCRYPTED_ID_RADIX);
      String decryptedString = new String(encryptor.decrypt(unpadBytes(bigint.toByteArray())));
      // The projectId is the first ID_DIGITS characters of the decrypted string
      return new BigInteger(decryptedString.substring(0, ID_DIGITS), 16).longValue();
    } catch (EncryptionException e) {  // COV_NF_LINE
      LOG.log(Level.SEVERE, "Decryption failed with error", e);
      return StorageIo.INVALID_PROJECTID;  // COV_NF_LINE
    } catch (NumberFormatException e) {
      return StorageIo.INVALID_PROJECTID;
    }
  }

  /*
   * Prepend a non-zero byte to the beginning of the byte array and return the
   * new array.
   */
  private static byte[] padBytes(byte[] byteArray) {
    byte[] paddedBytes = new byte[byteArray.length + 1];
    paddedBytes[0] = 0x1; // anything non-zero is good
    for (int i = 0; i < byteArray.length; i++) {
      paddedBytes[i+1] = byteArray[i];
    }
    return paddedBytes;
  }

  /*
   * Remove the first byte from the beginning of the byte array and return the
   * new array.
   */
  private static byte[] unpadBytes(byte[] paddedByteArray) {
    return java.util.Arrays.copyOfRange(paddedByteArray, 1, paddedByteArray.length);
  }
}
