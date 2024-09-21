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
    
    // ========== 定时任务 1-001-001-000 ==========
    ErrorCode JOB_NOT_EXISTS = new ErrorCode(1_001_001_000, "定时任务不存在");
    ErrorCode JOB_HANDLER_EXISTS = new ErrorCode(1_001_001_001, "定时任务的处理器已经存在");
    ErrorCode JOB_CHANGE_STATUS_INVALID = new ErrorCode(1_001_001_002, "只允许修改为开启或者关闭状态");
    ErrorCode JOB_CHANGE_STATUS_EQUALS = new ErrorCode(1_001_001_003, "定时任务已经处于该状态，无需修改");
    ErrorCode JOB_UPDATE_ONLY_NORMAL_STATUS = new ErrorCode(1_001_001_004, "只有开启状态的任务，才可以修改");
    ErrorCode JOB_CRON_EXPRESSION_VALID = new ErrorCode(1_001_001_005, "CRON 表达式不正确");
    ErrorCode JOB_HANDLER_BEAN_NOT_EXISTS = new ErrorCode(1_001_001_006, "定时任务的处理器 Bean 不存在");
    ErrorCode JOB_HANDLER_BEAN_TYPE_ERROR = new ErrorCode(1_001_001_007, "定时任务的处理器 Bean 类型不正确，未实现 JobHandler 接口");

}
