package cn.mutils.app.archive;

import java.io.InputStream;

import cn.mutils.app.core.archive.Zip;
import cn.mutils.app.core.archive.ZipUtil;

public class ZipDelegate extends Zip {

    @Override
    public byte[] getBytes(InputStream is, String entryName) {
        return ZipUtil.getBytes(is, entryName);
    }

}
