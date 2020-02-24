/**
 * 
 */
package com.tangdao.framework.protocol;

import java.util.LinkedHashMap;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月21日
 */

public class Result extends LinkedHashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "message";

    private static final String SUCCESS = "success";

    private static final String DATA = "data";

    private static final String REDIRECT = "redirect";

    private static final String EMPTY = "";
    
    private static final String STATUS = "status";

    private Result() {
    	super();
    	this.put(SUCCESS, false);
    }
    
    public String getMessage() {
        if (get(MESSAGE) != null) {
            return (String) get(MESSAGE);
        }
        return EMPTY;
    }

    public Result success() {
        this.put(SUCCESS, true);
        return this;
    }

    public Result success(String message) {
        this.put(SUCCESS, true);
        this.put(MESSAGE, message);
        return this;
    }

    public Result fail(String message) {
        this.put(SUCCESS, false);
        this.put(MESSAGE, message);
        return this;
    }

    public Result redirect(String url) {
        this.put(REDIRECT, url);
        return this;
    }

    public Result setData(Object data) {
        return putData(DATA, data);
    }

    public Result putData(String key, Object data) {
        this.put(key, data);
        return this;
    }

    public static Result createResult() {
        Result result = new Result();
        result.success();
        return result;
    }

    public static Result createResult(Object data) {
        Result result = new Result();
        result.success();
        result.setData(data);
        return result;
    }
    
    public static Result createResult(IEnum<?, ?> status) {
        Result result = new Result();
        result.put(STATUS, status.value());
        result.put(MESSAGE, status.reasonPhrase());
        return result;
    }

}
