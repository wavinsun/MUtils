package cn.mutils.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

import cn.mutils.core.runtime.OSRuntime;

/**
 * File utility
 */
@SuppressWarnings("unused")
public class FileUtil {

    public static boolean setHidden(File file, boolean isHidden) {
        if (file.isHidden() == isHidden) {
            return true;
        }
        if (OSRuntime.WINDOWS == OSRuntime.getOSRuntime()) {
            try {
                ProcessBuilder pb = new ProcessBuilder("attrib", isHidden ? "+h" : "-h", file.getCanonicalPath());
                Process p = pb.start();
                return p.waitFor() == 0;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static byte[] getBytes(File f) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            return IOUtil.getBytes(fis);
        } catch (Exception e) {
            return null;
        } finally {
            IOUtil.close(fis);
        }
    }

    public static boolean putBytes(File f, byte[] bytes) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(f, "rw");
            raf.setLength(bytes.length);
            raf.write(bytes);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            IOUtil.close(raf);
        }
    }

    public static boolean equals(File f, byte[] bytes) {
        if (bytes == null || !f.isFile() || bytes.length != f.length()) {
            return false;
        }
        return Arrays.equals(getBytes(f), bytes);
    }

}
