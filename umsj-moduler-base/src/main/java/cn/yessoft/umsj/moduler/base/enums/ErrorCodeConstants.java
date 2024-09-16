package cn.yessoft.umsj.moduler.base.enums;


import cn.yessoft.umsj.common.exception.ErrorCode;

/**
 * System 错误码枚举类
 * <p>
 * system 系统，使用 1-002-000-000 段
 */
public interface ErrorCodeConstants {
    // ========== AUTH 模块 1-002-000-000 ==========
    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1_002_000_000, "登录失败，账号密码不正确");
    ErrorCode PASSWORD_WRONG = new ErrorCode(1_002_000_001, "账号密码不正确");
    // ========== Base 模块 1-002-001-000 ==========
    ErrorCode DATA_NOT_EXISTS = new ErrorCode(1_002_001_000, "找不到数据");
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1_002_001_001, "用户不存在");
    ErrorCode FILE_NOT_EXISTS = new ErrorCode(1_002_001_002, "文件不存在");
    //  账户管理
    ErrorCode AID_DUPLICATE = new ErrorCode(1_002_001_003, "该账号已存在");
    //  通用
    ErrorCode O_DUPLICATE = new ErrorCode(1_002_001_004, "该{}已存在");
    ErrorCode O_NOT_EXISTS = new ErrorCode(1_002_001_005, "找不到该{}");
}
