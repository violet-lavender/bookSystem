package com.exp.config;

public class AppConfig {
    // 默认黑名单时长（以天为单位）
    public static final int DEFAULT_BLACKLIST_DURATION_DAYS = 15;

    // 默认黑名单逾期次数
    public static final int DEFAULT_BLACKLIST_OVERDUE_LIMIT = 5;

    // 默认逾期提醒次数
    public static final int DEFAULT_REMINDER_LIMIT = 3;

    // 默认续借时长（以天为单位）
    public static final int DEFAULT_RENEWAL_DURATION_DAYS = 10;

    private AppConfig() {
        // 私有构造方法防止实例化
    }
}
