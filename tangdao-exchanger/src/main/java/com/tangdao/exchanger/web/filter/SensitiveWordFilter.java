package com.tangdao.exchanger.web.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 思路： 创建一个FilterSet，枚举了0~65535的所有char是否是某个敏感词开头的状态 判断是否是 敏感词开头 | | 是 不是 获取头节点
 * OK--下一个字 然后逐级遍历，DFA算法
 * 
 * @author pangjs ~ 2015-4-29 下午06:30:29
 * @author tenx
 */
public class SensitiveWordFilter {

	// private static WordNode[] nodes = new
	// WordNode[65536];省6W个句柄的空间呗，测试发现，相比使用65536长度数组方式，过滤速度也提高了
	private static volatile WordFilterSet WORD_FILTER_SET = new WordFilterSet();
	private static volatile Map<Integer, WordNode> WORD_NODES = new HashMap<>(1024, 1);

	private static volatile Map<String, Pattern> WILDCARDS_WORDS = new HashMap<>();

	/**
	 * 通配敏感词标记
	 */
	public static final String WILDWORDS_CHARACTOR = ".*";

	/**
	 * 敏感词替换的符号
	 */
	private static final char SIGN = '*';

	private static final Logger logger = LoggerFactory.getLogger(SensitiveWordFilter.class);

	// 当前敏感词初始化词库来源于REDIS或者数据库
	// static {
	// try {
	// long a = System.nanoTime();
	// init();
	// a = System.nanoTime() - a;
	// System.out.println("加载时间 : " + a + "ns");
	// System.out.println("加载时间 : " + a / 1000000 + "ms");
	// } catch (Exception e) {
	// throw new RuntimeException("初始化过滤器失败");
	// }
	// }

	/**
	 * TODO 敏感词是否为通配敏感词
	 * 
	 * @param words
	 * @return
	 */
	public static boolean isWildcardsWords(String words) {
		return StringUtils.isNotEmpty(words) && words.contains(WILDWORDS_CHARACTOR);
	}

	/**
	 * TODO 加载所有通配符敏感词库
	 * 
	 * @param wordsCol
	 */
	public static void loadWildcarsWords(Collection<String> wordsCol) {
		for (String words : wordsCol) {
			if (!isWildcardsWords(words)) {
				continue;
			}

			WILDCARDS_WORDS.put(words, Pattern.compile(words));
		}
	}

	/**
	 * TODO 增加通配敏感词
	 * 
	 * @param wildcardsWords
	 */
	public static void addWildcarsWords(String wildcardsWords) {
		WILDCARDS_WORDS.put(wildcardsWords, Pattern.compile(wildcardsWords));
	}

	/**
	 * TODO 移除通配敏感词
	 * 
	 * @param wildcardsWords
	 */
	public static void removeWildcarsWords(String wildcardsWords) {
		WILDCARDS_WORDS.remove(wildcardsWords);
	}

	/**
	 * TODO 根据通配敏感词摘取短信内容符合通配敏感词的数据
	 * 
	 * @param content
	 * @return
	 */
	public static Set<String> pickupWildcardsWords(String content) {
		if (StringUtils.isEmpty(content)) {
			return null;
		}

		try {
			Set<String> finalWords = new HashSet<>();
			for (Entry<String, Pattern> wordsPattern : WILDCARDS_WORDS.entrySet()) {
				if (isMatched(wordsPattern.getValue(), content)) {
					finalWords.add(wordsPattern.getKey());
				}
			}

			return finalWords;
		} catch (Exception e) {
			logger.error("content : [" + content + "] 摘取通配敏感词失败", e);
			return null;
		}
	}

	/**
	 * TODO 正则表达式是否匹配
	 * 
	 * @param pattern
	 * @param content
	 * @return
	 */
	private static boolean isMatched(Pattern pattern, String content) {
		try {
			return pattern.matcher(content).matches();
		} catch (Exception e) {
			logger.error("content : [" + content + "] 匹配异常", e);
			return false;
		}
	}

	/**
	 * TODO 初始化词条
	 * 
	 * @param words
	 */
	public static void setSensitiveWord(List<String> words) {
		WORD_FILTER_SET = new WordFilterSet();
		WORD_NODES = new HashMap<Integer, WordNode>(1024, 1);

		char[] chs;
		int fchar;
		int lastIndex;
		WordNode fnode;
		for (String curr : words) {
			chs = curr.toCharArray();
			fchar = chs[0];
			// 没有首字定义
			if (!WORD_FILTER_SET.contains(fchar)) {
				WORD_FILTER_SET.add(fchar);
				// 首字标志位 可重复add,反正判断了，不重复了
				fnode = new WordNode(fchar, chs.length == 1);
				WORD_NODES.put(fchar, fnode);
			} else {
				fnode = WORD_NODES.get(fchar);
				if (!fnode.isLast() && chs.length == 1) {
					fnode.setLast(true);
				}
			}
			lastIndex = chs.length - 1;
			for (int i = 1; i < chs.length; i++) {
				fnode = fnode.addIfNoExist(chs[i], i == lastIndex);
			}
		}
	}

	/**
	 * TODO 过滤并替换为*
	 * 
	 * @param src
	 * @return
	 */
	public static final String doFilterAndReplace(final String src) {
		char[] chs = src.toCharArray();
		int length = chs.length;
		int currc;
		int k;
		WordNode node;
		for (int i = 0; i < length; i++) {
			currc = chs[i];
			if (!WORD_FILTER_SET.contains(currc)) {
				continue;
			}
			// k=i;//日 2
			node = WORD_NODES.get(currc);// 日 2
			if (node == null)// 其实不会发生，习惯性写上了
			{
				continue;
			}
			boolean couldMark = false;
			int markNum = -1;
			if (node.isLast()) {// 单字匹配（日）
				couldMark = true;
				markNum = 0;
			}
			// 继续匹配（日你/日你妹），以长的优先
			// 你-3 妹-4 夫-5
			k = i;
			for (; ++k < length;) {
				node = node.querySub(chs[k]);
				if (node == null)// 没有了
				{
					break;
				}
				if (node.isLast()) {
					couldMark = true;
					markNum = k - i;// 3-2
				}
			}
			if (couldMark) {
				for (k = 0; k <= markNum; k++) {
					chs[k + i] = SIGN;
				}
				i = i + markNum;
			}
		}
		return new String(chs);
	}

	/**
	 * TODO 过滤敏感词并返回最终的敏感词汇
	 * 
	 * @param src          源数据
	 * @param wordsLibrary 敏感词库
	 * @return
	 */
	public static final Set<String> doFilter(final String src) {
		char[] chs = src.toCharArray();
		int length = chs.length;
		int currc;
		int k;
		WordNode node;
		Set<String> words = new HashSet<>();
		for (int i = 0; i < length; i++) {
			currc = chs[i];
			if (!WORD_FILTER_SET.contains(currc)) {
				continue;
			}
			// k=i;//日 2
			node = WORD_NODES.get(currc);// 日 2
			if (node == null)// 其实不会发生，习惯性写上了
			{
				continue;
			}
			boolean couldMark = false;
			int markNum = -1;
			if (node.isLast()) {// 单字匹配（日）
				couldMark = true;
				markNum = 0;
			}
			// 继续匹配（日你/日你妹），以长的优先
			// 你-3 妹-4 夫-5
			k = i;
			for (; ++k < length;) {
				node = node.querySub(chs[k]);
				if (node == null)// 没有了
				{
					break;
				}
				if (node.isLast()) {
					couldMark = true;
					markNum = k - i;// 3-2
				}
			}
			if (couldMark) {
				if (markNum > 0) {
					StringBuilder fword = new StringBuilder();
					for (k = 0; k <= markNum; k++) {
						fword.append(chs[k + i]);
					}
					if (StringUtils.isNotEmpty(fword)) {
						words.add(fword.toString());
					}
				}
				i = i + markNum;
			}
		}
		return words;
	}

	/**
	 * 是否包含敏感词
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isContains(final String src) {
		char[] chs = src.toCharArray();
		int length = chs.length;
		int currc;
		int k;
		WordNode node;
		for (int i = 0; i < length; i++) {
			currc = chs[i];
			if (!WORD_FILTER_SET.contains(currc)) {
				continue;
			}
			// k=i;//日 2
			node = WORD_NODES.get(currc);// 日 2
			if (node == null)// 其实不会发生，习惯性写上了
			{
				continue;
			}
			if (node.isLast()) {// 单字匹配（日）
				return true;
			}
			// 继续匹配（日你/日你妹），以长的优先
			// 你-3 妹-4 夫-5
			k = i;
			for (; ++k < length;) {
				node = node.querySub(chs[k]);
				if (node == null)// 没有了
				{
					break;
				}
				if (node.isLast()) {
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {

		// 初始化词条
		List<String> words = new ArrayList<String>();
		words.add("卫生巾");

		setSensitiveWord(words);

		String s = "【全棉时代天猫旗舰店】520亲子节，卫生巾化妆棉爆款直降50%OFF！￥品质棉，新生爱￥（复制整段再打开手机淘宝抢先看）退订回N";
		System.out.println("解析字数 : " + s.length());
		long nano = System.nanoTime();

		Set<String> senwords = SensitiveWordFilter.doFilter(s);
		nano = (System.nanoTime() - nano);
		System.out.println("解析时间 : " + nano + "ns");
		System.out.println("解析时间 : " + nano / 1000000 + "ms");
		System.out.println(senwords);
		System.out.println(senwords.size());

		words.remove("卫生巾");
		setSensitiveWord(words);

		senwords = SensitiveWordFilter.doFilter(s);
		System.out.println(senwords);
		System.out.println(senwords.size());
	}

}
