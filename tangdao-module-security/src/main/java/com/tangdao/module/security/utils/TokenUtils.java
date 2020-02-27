/**
 * 
 */
package com.tangdao.module.security.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tangdao.framework.constant.OpenApiCode.ResultStatus;
import com.tangdao.framework.model.UserInfo;
import com.tangdao.module.security.exception.SecurityException;
import com.tangdao.module.security.service.UserPrincipal;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */
@Component
public class TokenUtils {
	
	/**
     * Token 类型
     */
    public static final String TOKEN_TYPE_BEARER = "Bearer";
    
    /**
     * 
     */
    public static final String SECRET_KEY = "ABCD1234";
    
    /**
     * 12h
     */
	public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 43200;
	
	/**
	 * 30d
	 */
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 2592000;
    
    /**
     * 
     */
    public Logger logger = LoggerFactory.getLogger(getClass());

	/**
     * 创建JWT
     *
     * @param rememberMe  记住我
     * @param id          用户id
     * @param subject     用户名
     * @param tenantId    租户id
     * @param authorities 用户权限
     * @return JWT
     */
    public String createJWT(Boolean rememberMe, UserInfo user) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .setId(user.getId())
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .claim("roles", user.getRoles())
                .claim("tenantId", user.getTenantId());

        // 设置过期时间
        int ttl = rememberMe ? REFRESH_TOKEN_VALIDITY_SECONDS : ACCESS_TOKEN_VALIDITY_SECONDS;
        if (ttl > 0) {
            builder.setExpiration(DateUtil.offsetMillisecond(now, ttl*1000));
        }

        String jwt = builder.compact();
        // 将生成的JWT保存至Redis
//        stringRedisTemplate.opsForValue().set(Consts.REDIS_JWT_KEY_PREFIX + subject, jwt, ttl*1000 , TimeUnit.MILLISECONDS);
        return jwt;
    }

    /**
     * 创建JWT
     *
     * @param authentication 用户认证信息
     * @param rememberMe     记住我
     * @return JWT
     */
    public String createJWT(Authentication authentication, Boolean rememberMe) {
        return createJWT(rememberMe, ((UserPrincipal) authentication.getPrincipal()).getUser());
    }

    /**
     * 解析JWT
     *
     * @param jwt JWT
     * @return {@link Claims}
     */
    public Claims parseJWT(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();

//            String username = claims.getSubject();
//            String redisKey = Consts.REDIS_JWT_KEY_PREFIX + username;
//
//            // 校验redis中的JWT是否存在
//            Long expire = stringRedisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS);
//            if (Objects.isNull(expire) || expire <= 0) {
//                throw new SecurityException(ResultStatus.TOKEN_EXPIRED);
//            }
//
//            // 校验redis中的JWT是否与当前的一致，不一致则代表用户已注销/用户在不同设备登录，均代表JWT已过期
//            String redisToken = stringRedisTemplate.opsForValue().get(redisKey);
//            if (!StrUtil.equals(jwt, redisToken)) {
//                throw new SecurityException(ResultStatus.TOKEN_OUT_OF_CTRL);
//            }
            return claims;
        } catch (ExpiredJwtException e) {
        	logger.error("Token 已过期");
            throw new SecurityException(ResultStatus.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
        	logger.error("Token 不支持");
            throw new SecurityException(ResultStatus.TOKEN_PARSE_ERROR);
        } catch (MalformedJwtException e) {
        	logger.debug("Token 无效");
            throw new SecurityException(ResultStatus.TOKEN_PARSE_ERROR);
        } catch (SignatureException e) {
        	logger.debug("Token 签名无效");
            throw new SecurityException(ResultStatus.TOKEN_PARSE_ERROR);
        } catch (IllegalArgumentException e) {
        	logger.error("Token 参数异常");
            throw new SecurityException(ResultStatus.TOKEN_PARSE_ERROR);
        }
    }

    /**
     * 设置JWT过期
     *
     * @param request 请求
     */
    public void invalidateJWT(HttpServletRequest request) {
//        String jwt = getJwtFromRequest(request);
//        String username = getUsernameFromJWT(jwt);
        // 从redis中清除JWT
//        stringRedisTemplate.delete(Consts.REDIS_JWT_KEY_PREFIX + username);
    }

    /**
     * 根据 jwt 获取用户名
     *
     * @param jwt JWT
     * @return 用户名
     */
    public String getUsernameFromJWT(String jwt) {
        Claims claims = parseJWT(jwt);
        return claims.getSubject();
    }

    /**
     * 从 request 的 header 中获取 JWT
     *
     * @param request 请求
     * @return JWT
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith(TOKEN_TYPE_BEARER + " ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
