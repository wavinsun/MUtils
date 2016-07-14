package cn.mutils.app.demo.impl;

import cn.mutils.app.demo.IUserService;
import cn.mutils.core.runtime.StackTraceUtil;

public class UserServiceImpl implements IUserService {

    @Override
    public String getUserName() {
        return this.getClass().getName() + "#" + StackTraceUtil.getCurrentElement().getMethodName();
    }

}
