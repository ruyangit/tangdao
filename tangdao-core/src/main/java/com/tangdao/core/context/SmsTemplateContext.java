/**
 * 
 */
package com.tangdao.core.context;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年3月12日
 */
public class SmsTemplateContext {

	// 无需检验模板模板ID标识
    public static final long SUPER_TEMPLATE_ID       = 0L;

    // 默认短信提交间隔数（同一手机号码）
    public static final int  DEFAULT_SUBMIT_INTERVAL = 30;
    // 默认同一手机号码同一天提交上限次数
    public static final int  DEFAULT_LIMIT_TIMES     = 10;

    /**
     * TODO 审批状态
     * 
     * @author zhengying
     * @version V1.0
     * @date 2016-01-17 下午03:20:35
     */
    public enum ApproveStatus {
        WAITING(0, "待审批"), SUCCESS(1, "审批通过"), FAIL(2, "审批失败");

        private int    value;
        private String title;

        private ApproveStatus(int value, String title) {
            this.value = value;
            this.title = title;
        }

        public static ApproveStatus parse(int value) {
            for (ApproveStatus as : ApproveStatus.values()) {
                if (as.getValue() == value) {
                    {
                        return as;
                    }
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }

        public String getTitle() {
            return title;
        }
    }

    /**
     * 模板类型
     * 
     * @author Administrator
     */
    public enum MessageTemplateType {
        VERIFICATIONCODE(0, "验证码模板"), MARKETING(1, "营销模板"), OTER(2, "其它");

        private int    value;
        private String title;

        private MessageTemplateType(int value, String title) {
            this.value = value;
            this.title = title;
        }

        public int getValue() {
            return value;
        }

        public String getTitle() {
            return title;
        }
    }

    /**
     * 操作方式，1:融合WEB平台,2:开发者平台,3:运营支撑系统
     * 
     * @author Administrator
     */
    public enum ModeOperation {
        FUSIONWEB(1, "融合WEB平台"), DEVELOPERS(2, "开发者平台"), OPERATIONSUPPORT(3, "运营支撑系统");

        private int    value;
        private String title;

        private ModeOperation(int value, String title) {
            this.value = value;
            this.title = title;
        }

        public int getValue() {
            return value;
        }

        public String getTitle() {
            return title;
        }
    }

    /**
     * 
      * TODO 忽略黑名单拦截
      * 
      * @author zhengying
      * @version V1.0   
      * @date 2018年5月2日 下午5:03:05
     */
    public enum IgnoreBlacklist {
        /**
         * 拦截黑名单
         */
        NO(0), 
        
        /**
         * 放行黑名单
         */
        YES(1);

        private int value;

        private IgnoreBlacklist(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 
      * TODO 忽略敏感词拦截设置
      * 
      * @author zhengying
      * @version V1.0   
      * @date 2018年8月18日 下午5:29:44
     */
    public enum IgnoreForbiddenWords {
        /**
         * 拦截黑名单
         */
        NO(0), 
        
        /**
         * 放行黑名单
         */
        YES(1);

        private int value;

        private IgnoreForbiddenWords(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
