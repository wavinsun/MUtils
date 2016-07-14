package cn.mutils.core.runtime;

public class CC {

    /**
     * Get instance of interface
     *
     * If name of interface is "cn.mutils.demo.IUserInfoService", Return instance name of class is
     * "cn.mutils.demo.impl.UserInfoServiceImpl"
     *
     * @param clazz Interface definition
     * @param <T>   Class whose name should be keep for proguard
     * @return Implementation object
     */
    public static <T extends IService> T getService(Class<T> clazz) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(clazz.getPackage().getName());
            sb.append(".impl.");
            sb.append(clazz.getSimpleName().substring(1));
            sb.append("Impl");
            return (T) Class.forName(sb.toString()).newInstance();
        } catch (Exception e) {
            return null;
        }
    }

}
