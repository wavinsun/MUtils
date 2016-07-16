package cn.mutils.core.mozilla;

import cn.mutils.core.runtime.IService;

public interface ICharsetDetector extends IService {

    String getCharset(byte[] bytes);

}
