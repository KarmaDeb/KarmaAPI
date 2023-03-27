package ml.karmaconfigs.api.common.security.file;

/*
 * This file is part of KarmaAPI, licensed under the MIT License.
 *
 *  Copyright (c) karma (KarmaDev) <karmaconfigs@gmail.com>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

import ml.karmaconfigs.api.common.string.random.RandomString;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * File encryption utilities
 */
public class FileEncryptor {

    private final Path file;
    private final SecretKey key;

    /**
     * Generate a secure key used for encryption
     *
     * @param token the key token
     * @param salt the key salt
     * @return the secure secret key
     */
    public static SecretKey generateSecureKey(final String token, final String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(token.toCharArray(), salt.getBytes(), 65536, 256);

            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Generate a secure IV for our encryption
     *
     * @return the secure parameter spec
     */
    public static IvParameterSpec generateSecureSpec() {
        SecureRandom random;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException error) {
            random = new SecureRandom();
        }

        byte[] salt = new byte[256];
        random.nextBytes(salt);

        return new IvParameterSpec(salt);
    }

    /**
     * Initialize the file encryptor
     *
     * @param tar the target file
     * @param token the file key
     */
    public FileEncryptor(final File tar, final byte[] token) {
        file = tar.toPath();
        key = new SecretKeySpec(token, 0, token.length, "AES");
    }

    /**
     * Initialize the file encryptor
     *
     * @param tar the target file
     * @param token the file key
     */
    public FileEncryptor(final Path tar, final byte[] token) {
        file = tar;
        key = new SecretKeySpec(token, 0, token.length, "AES");
    }

    /**
     * Transform the file
     *
     * @param mode the mode
     * @param iv the encryption iv
     * @throws IOException as part of implementation
     * @throws NoSuchAlgorithmException as part of implementation
     * @throws NoSuchPaddingException as part of implementation
     * @throws InvalidKeyException as part of implementation
     * @throws IllegalBlockSizeException as part of implementation
     * @throws BadPaddingException as part of implementation
     */
    protected void transform(final int mode, final IvParameterSpec iv) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Path tmp = Files.createTempFile("file_encryptor" + new RandomString().create(), ".tmp");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(mode, key);

        try (InputStream stream = Files.newInputStream(file)) {
            try (OutputStream output = Files.newOutputStream(tmp)) {
                byte[] buffer = new byte[4096];
                int read;

                while ((read = stream.read(buffer)) != -1) {
                    byte[] outBytes = cipher.update(buffer, 0, read);
                    output.write(outBytes);
                }

                byte[] finalBites = cipher.doFinal();
                output.write(finalBites);

                Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /**
     * Encrypt the file
     *
     * @param iv the encryption iv
     * @return if the file could be encrypted
     */
    public boolean encrypt(final IvParameterSpec iv) {
        try {
            transform(Cipher.ENCRYPT_MODE, iv);
            return true;
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ignored) {}

        return false;
    }

    /**
     * Decrypt the file
     *
     * @param iv the encryption iv
     * @return if the file could be decrypted
     */
    public boolean decrypt(final IvParameterSpec iv) {
        try {
            transform(Cipher.DECRYPT_MODE, iv);
            return true;
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ignored) {}

        return false;
    }

    /**
     * Tries to encrypt the file
     *
     * @param iv the encryption iv
     * @throws IOException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     * @throws NoSuchAlgorithmException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     * @throws NoSuchPaddingException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     * @throws InvalidKeyException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     * @throws IllegalBlockSizeException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     * @throws BadPaddingException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     */
    public void tryEncrypt(final IvParameterSpec iv) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        transform(Cipher.ENCRYPT_MODE, iv);
    }

    /**
     * Tries to decrypt the file
     *
     * @param iv the encryption iv
     * @throws IOException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     * @throws NoSuchAlgorithmException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     * @throws NoSuchPaddingException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     * @throws InvalidKeyException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     * @throws IllegalBlockSizeException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     * @throws BadPaddingException as part of {@link FileEncryptor#transform(int, IvParameterSpec)}
     */
    public void tryDecrypt(final IvParameterSpec iv) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        transform(Cipher.DECRYPT_MODE, iv);
    }
}
