package cn.o.app.io;

import java.io.Serializable;

/**
 * No ProGuard for class who implements me
 * 
 * Do not forget give settings of "proguard-project.txt":
 * 
 * -keep interface {@link INoProguard} {*;}
 * 
 * -keep interface * extends {@link INoProguard} {*;}
 * 
 * -keep class * implements {@link INoProguard} {*;}
 * 
 * -keepclasseswithmembernames class * implements {@link INoProguard} {*;}
 */
public interface INoProguard extends Serializable {

}
