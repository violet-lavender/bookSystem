package com.exp.config;

public class AppConfig {

    // 默认pageSize
    public static final String DEFAULT_PAGE_SIZE = "100";

    // 默认黑名单时长（以天为单位）
    public static final long DEFAULT_BLACKLIST_DURATION_DAYS = 15;

    // 默认黑名单逾期次数
    public static final int DEFAULT_BLACKLIST_OVERDUE_LIMIT = 5;

    // 默认逾期提醒次数
    public static final int DEFAULT_REMINDER_LIMIT = 3;

    // 默认借书时长（以天为单位）
    public static final int DEFAULT_LEND_DURATION_DAYS = 30;

    // 默认续借时长（以天为单位）
    public static final int DEFAULT_RENEWAL_DURATION_DAYS = 10;

    // 系统操作默认参数
    public static final String DEFAULT_OPERATE_ROLE = "SYSTEM";
    public static final Integer DEFAULT_OPERATE_USER = -1;
    public static final String DEFAULT_OPERATE_USERNAME = "System";

    public static final Integer DEFAULT_RECOMMEND_PARAM_K = 10;
    public static final Integer DEFAULT_RECOMMEND_PARAM_N = 5;

    private AppConfig() {
        // 私有构造方法防止实例化
    }
}
