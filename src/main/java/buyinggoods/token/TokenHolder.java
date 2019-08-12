package buyinggoods.token;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.|
 * |                     ______                     |
 * |                  .-"      "-.                  |
 * |                 /            \                 |
 * |     _          |              |          _     |
 * |    ( \         |,  .-.  .-.  ,|         / )    |
 * |     > "=._     | )(__/  \__)( |     _.=" <     |
 * |    (_/"=._"=._ |/     /\     \| _.="_.="\_)    |
 * |           "=._"(_     ^^     _)"_.="           |
 * |               "=\__|IIIIII|__/="               |
 * |              _.="| \IIIIII/ |"=._              |
 * |    _     _.="_.="\          /"=._"=._     _    |
 * |   ( \_.="_.="     `--------`     "=._"=._/ )   |
 * |    > _.="                            "=._ <    |
 * |   (_/              NO  BUG  !            \_)   |
 * |                                                |
 * '-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='
 *
 * @author chenjiandong
 * @description: TODO
 * @date 2019/8/7 10:10
 */
public class TokenHolder {
    public static final String ASR_SCOPE = "audio_voice_assistant_get";

    public static final String TTS_SCOPE = "audio_tts_post";

    /**
     * URL , Token的url，http可以改为https
     */
    private static final String URL = "http://openapi.baidu.com/oauth/2.0/token";

    /**
     * asr的权限 scope 是  "audio_voice_assistant_get"
     * tts 的权限 scope 是 "audio_tts_post"
     */
    private String scope;

    /**
     * 网页上申请语音识别应用获取的apiKey
     */
    private String apiKey;

    /**
     * 网页上申请语音识别应用获取的secretKey
     */
    private String secretKey;

    /**
     * 保存访问接口获取的token
     */
    private String token;

    /**
     * 当前的时间戳，毫秒
     */
    private long expiresAt;

    /**
     * @param apiKey    网页上申请语音识别应用获取的apiKey
     * @param secretKey 网页上申请语音识别应用获取的secretKey
     */
    public TokenHolder(String apiKey, String secretKey, String scope) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.scope = scope;
    }

    /**
     * 获取token，refresh 方法后调用有效
     *
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     * 获取过期时间，refresh 方法后调用有效
     *
     * @return
     */
    public long getExpiresAt() {
        return expiresAt;
    }
    /**
     * 获取token
     *
     * @return
     * @throws IOException   http请求错误
     */
    public void refresh() throws Exception {
        String getTokenURL = URL + "?grant_type=client_credentials"
                + "&client_id=" + ConnUtil.urlEncode(apiKey) + "&client_secret=" + ConnUtil.urlEncode(secretKey);

        // 打印的url出来放到浏览器内可以复现
        System.out.println("token URL:" + getTokenURL);

        URL urlconn = new URL(getTokenURL);
        HttpURLConnection conn = (HttpURLConnection) urlconn.openConnection();
        conn.setConnectTimeout(5000);
        String result = ConnUtil.getResponseString(conn);
        System.out.println("Token result json:" + result);
        parseJson(result);
    }

    /**
     * @param result token接口获得的result
     */
    private void parseJson(String result) throws Exception {
        JSONObject json = new JSONObject(result);
        if (!json.has("access_token")) {
            // 返回没有access_token字段
            throw new Exception("access_token not obtained, " + result);
        }
        if (!json.has("scope")) {
            // 返回没有scope字段
            throw new Exception("scope not obtained, " + result);
        }
        if (!json.getString("scope").contains(scope)) {
            throw new Exception("scope not exist, " + scope + "," + result);
        }
        token = json.getString("access_token");
        expiresAt = System.currentTimeMillis() + json.getLong("expires_in") * 1000;
    }
}