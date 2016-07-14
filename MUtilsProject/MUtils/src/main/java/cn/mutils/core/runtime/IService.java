package cn.mutils.core.runtime;

import proguard.annotation.KeepImplementations;
import proguard.annotation.KeepName;

/**
 * Service top level definition
 *
 * If you have a interface of "cn.mutils.app.demo.IUserInfoService" extends IService, The
 * implementation path must be "cn.mutils.app.demo.impl.UserInfoServiceImpl"
 */
@KeepName
@KeepImplementations
public interface IService {

}
