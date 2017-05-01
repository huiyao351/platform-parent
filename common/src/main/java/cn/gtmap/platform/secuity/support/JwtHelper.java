package cn.gtmap.platform.secuity.support;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 处理jws，json web token
 * Created by JIBO on 2016/5/17.
 */
public class JwtHelper {
    static final Logger logger= LoggerFactory.getLogger(JwtHelper.class);
    //static String JWT_SHORTNAME = "GTMAP CAS";
    //I long for education equity in China
    static String JWT_SECRET = "SSUyMGxvbmclMjBmb3IlMjBlZHVjYXRpb24lMjBlcXVpdHklMjBpbiUyMENoaW5h";


    /**
     * 根据userid和username得到token
     * @param userId
     * @param userName
     * @return
     */
    public static String buildUserLoginToken(String userId,String userName){
        //Key key = MacProvider.generateKey();
        Claims claims = Jwts.claims().setSubject(userName);
        claims.setId(userId);
        //claims.put("userId",userId);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }


    /**
     * 根据token得到userid和username的数组
     * @param token
     * @return
     */
    public static String[] getUserByToken(String token){
        try {
            Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
            return new String[]{claims.getId(), claims.getSubject()};
        }catch (Exception ex){
            logger.error("解析客户端cookie中的token出错！",ex);

        }
        return null;
    }

    public static void main(String [] args){
        String token=buildUserLoginToken("110","用户A");
        System.out.println("Serialised JWS object: " + token);
        String[] user= getUserByToken(token);
        System.out.println("userId: " +user[0]);
    }
}
