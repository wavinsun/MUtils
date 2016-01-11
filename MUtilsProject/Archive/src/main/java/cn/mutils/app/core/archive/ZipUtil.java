package cn.mutils.app.core.archive;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.mutils.app.core.io.IOUtil;

/**
 * Fix bug for Archive file by Apache Commons Compress
 */
@SuppressWarnings({"UnusedAssignment", "unused"})
public class ZipUtil {

    /**
     * Get byte array from stream by entry path
     */
    public static byte[] getBytes(InputStream is, String entry) {
        ZipArchiveInputStream zais = null;
        ByteArrayOutputStream bos = null;
        try {
            zais = new ZipArchiveInputStream(is, "GBK");
            ZipArchiveEntry zae = null;
            while ((zae = zais.getNextZipEntry()) != null) {
                if (!zae.getName().equals(entry)) {
                    continue;
                }
                if (zae.isDirectory()) {
                    return null;
                }
                bos = new ByteArrayOutputStream();
                IOUtil.copy(zais, bos);
                return bos.toByteArray();
            }
        } catch (Exception e) {
            return null;
        } finally {
            IOUtil.close(zais);
            IOUtil.close(bos);
        }
        return null;
    }

    /**
     * Get byte array from file by entry path
     */
    public static byte[] getBytes(String file, String entry) {
        File f = new File(file);
        if (!f.isFile()) {
            return null;
        }
        ZipFile zipFile = null;
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            zipFile = new ZipFile(f, "GBK");
            ZipArchiveEntry zipEntry = zipFile.getEntry(entry);
            if (zipEntry == null) {
                return null;
            }
            is = zipFile.getInputStream(zipEntry);
            bos = new ByteArrayOutputStream();
            IOUtil.copy(is, bos);
            return bos.toByteArray();
        } catch (Exception e) {
            return null;
        } finally {
            IOUtil.close(zipFile);
            IOUtil.close(is);
            IOUtil.close(bos);
        }
    }

    /**
     * Compress file or directory
     */
    public static boolean zip(String file) {
        File f = new File(file);
        if (!f.exists()) {
            return false;
        }
        FileOutputStream fos = null;
        ZipArchiveOutputStream zaos = null;
        try {
            fos = new FileOutputStream(f.getPath() + ".zip");
            zaos = new ZipArchiveOutputStream(fos);
            zaos.setEncoding("GBK");
            return zip(zaos, f, null);
        } catch (Exception e) {
            return false;
        } finally {
            if (zaos != null) {
                try {
                    zaos.finish();
                } catch (Exception e) {
                    //  IOException
                }
            }
            IOUtil.close(zaos);
            IOUtil.close(fos);
        }
    }

    /**
     * Put file into stream by parent entry path
     */
    protected static boolean zip(ZipArchiveOutputStream zaos, File file, String parentEntry) {
        StringBuilder sb = new StringBuilder();
        if (parentEntry != null && !parentEntry.isEmpty()) {
            sb.append(parentEntry);
            sb.append("/");
        }
        if (file.isFile()) {
            sb.append(file.getName());
            String entry = sb.toString();
            ZipArchiveEntry zae = new ZipArchiveEntry(file, entry);
            FileInputStream fis = null;
            try {
                zaos.putArchiveEntry(zae);
                fis = new FileInputStream(file);
                IOUtil.copy(fis, zaos);
            } catch (Exception e) {
                return false;
            } finally {
                IOUtil.close(fis);
                try {
                    zaos.closeArchiveEntry();
                } catch (Exception e) {
                    // IOException
                }
            }
            return true;
        } else if (file.isDirectory()) {
            if (parentEntry != null) {
                sb.append(file.getName());
            }
            String entry = sb.toString();
            for (File f : file.listFiles()) {
                zip(zaos, f, entry);
            }
            return true;
        }
        return false;
    }
}
