package cn.mutils.app.core.archive;

import java.io.InputStream;

import cn.mutils.app.core.runtime.Delegate;

public class Zip extends Delegate<Zip> {

    public static final String CLASS_DELEGATE = "cn.mutils.app.archive.ZipDelegate";

    @Override
    public String classDelegate() {
        return CLASS_DELEGATE;
    }

    public byte[] getBytes(InputStream is, String entryName) {
        return null;
    }

}
