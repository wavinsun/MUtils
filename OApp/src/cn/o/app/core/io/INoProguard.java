package cn.o.app.core.io;

import java.io.Serializable;

/**
 * No ProGuard for class who implements me.<br>
 * Do not forget give settings of "proguard-project.txt":<br>
 * -keep interface {@link INoProguard} {*;}<br>
 * -keep interface * extends {@link INoProguard} {*;}<br>
 * -keep class * implements {@link INoProguard} {*;}<br>
 * -keepclasseswithmembernames class * implements {@link INoProguard} {*;}
 */
public interface INoProguard extends Serializable {

}
