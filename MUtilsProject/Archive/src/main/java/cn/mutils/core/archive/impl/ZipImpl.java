package cn.mutils.core.archive.impl;

import java.io.InputStream;

import cn.mutils.core.archive.IZip;
import cn.mutils.core.archive.ZipUtil;

public class ZipImpl implements IZip {

    @Override
    public byte[] getBytes(InputStream is, String entryName) {
        return ZipUtil.getBytes(is, entryName);
    }

}
