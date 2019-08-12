package buyinggoods.token;

import org.json.JSONObject;
import com.baidu.aip.speech.AipSpeech;

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
 * @author chenjiandong
 * @description: TODO
 * @date 2019/8/6 17:37
 */
public class TranslateVoice {
    //设置APPID/AK/SK
    public static final String APP_ID = "15825769";
    public static final String API_KEY = "pl72W7ZWVGOGQVnwhWOr5H4f";
    public static final String SECRET_KEY = "BG6Z143VIByY0CajE1tz06jxM0ZjHvCq";

    public static void main(String[] args) {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        JSONObject res = client.asr("F://file//result.wav", "wav", 16000, null);
        String s = null;
        try{
            s = res.toString(2);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(s);
    }
}