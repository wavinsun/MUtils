package cn.mutils.core.archive;

import java.io.InputStream;

import cn.mutils.core.runtime.IService;

public interface IZip extends IService{

    byte[] getBytes(InputStream is, String entryName);

}
