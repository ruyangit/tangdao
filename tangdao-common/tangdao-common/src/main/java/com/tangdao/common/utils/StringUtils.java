/**
 * 
 */
package com.tangdao.common.utils;


import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * TODO 字符处理
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月28日
 */

public class StringUtils extends org.apache.commons.lang3.StringUtils{
    
    public static boolean isRegexMetaCharacter(char regexChar) {
        switch (regexChar) {
            case '^':
            case '$':
            case '.':
            case '|':
            case '[':
            case '+':
            case '\\':
            case '(':
            case ')':
            case '{':
                return true;
            default:
                return false;
        }
    }
    
    public static boolean containsMatchCharacter(String name) {
        return name.indexOf('*') != -1 || name.indexOf('?') != -1;
    }
    
    public static String patternFromGlob(String glob) {
        StringBuilder sb = new StringBuilder("^");
        int len = glob.length();
        for (int i = 0; i < len; i++) {
            char c = glob.charAt(i);
            if (c == '*') {
                sb.append(".*");
            } else if (c == '?') {
                sb.append('.');
            } else {
                if (isRegexMetaCharacter(c)) {
                    sb.append('\\');
                }
                sb.append(c);
            }
        }
        sb.append("$");
        return sb.toString();
    }
    
    public static boolean containsControlCharacter(String value) {
        
        // we're going to check if the string contains
        // any characters in the '00' through '1F' range
        // so anything smaller than a space
        
        int length = value.length();
        for (int i = 0; i < length; i++) {
            if (value.charAt(i) < ' ') {
                return true;
            }
        }
        return false;
    }
    
    public static boolean requestUriMatch(String uri, Set<String> uriSet,
            List<Pattern> uriList) {
        
        // first we're going to check if we have the uri in our set
        
        if (uriSet != null && uriSet.contains(uri)) {
            return true;
        }
        
        // if not in our set, we'll check our pattern list for a regex match
        
        if (uriList != null) {
            for (Pattern pattern : uriList) {
                Matcher matcher = pattern.matcher(uri);
                if (matcher.matches()) {
                    return true;
                }
            }
        }
        return false;
    }
}

