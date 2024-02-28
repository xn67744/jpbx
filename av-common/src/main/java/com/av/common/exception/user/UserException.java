package com.av.common.exception.user;

import com.av.common.exception.base.BaseException;

/**
 * 用户信息异常类
 * 
 * @author long
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
