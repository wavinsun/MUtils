package cn.mutils.core.mozilla.impl;

import org.mozilla.universalchardet.UniversalDetector;

import cn.mutils.core.mozilla.ICharsetDetector;

public class CharsetDetectorImpl implements ICharsetDetector {

    @Override
    public String getCharset(byte[] bytes) {
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(bytes, 0, bytes.length);
        detector.dataEnd();
        String dc = detector.getDetectedCharset();
        String[] charsets = new String[]{"UTF-8", "UTF-16LE", "UTF-16BE", "UTF-32LE", "UTF-32BE", "GBK"};
        for (String c : charsets) {
            if (c.equals(dc)) {
                return dc;
            }
        }
        return "GBK";
    }
}
