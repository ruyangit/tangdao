/**
 *
 */
package com.tangdao.core.constant;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
public class RabbitConstant {

	public static final String EXCHANGE_MQ = "exchange.mq";

	public static final String MQ_MESSAGE = "mq_message";

	public enum WordsPriority {
		L10(10, new String[] { "验证码", "动态码", "校验码" }), L5(5, new String[] { "分钟", "[中国移动]" }),
		L1(1, new String[] { "回复TD" }), DEFAULT(3, new String[] { "" });

		private int level;
		private String[] words;

		private WordsPriority(int level, String[] words) {
			this.level = level;
			this.words = words;
		}

		public int getLevel() {
			return level;
		}

		public String[] getWords() {
			return words;
		}

		/**
		 * TODO 获取关键词的优先级
		 * 
		 * @param content
		 * @return
		 */
		public static int getLevel(String content) {
			if (StrUtil.isEmpty(content)) {
				return WordsPriority.DEFAULT.getLevel();
			}

			for (WordsPriority wp : WordsPriority.values()) {
				for (String w : wp.getWords()) {
					if (content.contains(w)) {
						return wp.getLevel();
					}
				}

			}

			return WordsPriority.DEFAULT.getLevel();
		}

	}

}
