/**
 *
 */
package com.tangdao.core.auth;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public interface AuthManager {

	/**
     * Authentication of request, identify the user who request the resource.
     *
     * @param request where we can find the user information
     * @return user related to this request, null if no user info is found.
     * @throws AccessException if authentication is failed
     */
    User login(Object request) throws AccessException;

    /**
     * Authorization of request, constituted with resource and user.
     *
     * @param permission permission to auth
     * @param user     user who wants to access the resource.
     * @throws AccessException if authorization is failed
     */
    void auth(Permission permission, User user) throws AccessException;
}
