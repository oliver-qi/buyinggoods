package buyinggoods.service.impl;

import buyinggoods.mapper.ProductMapper;
import buyinggoods.mapper.PurchaseRecordMapper;
import buyinggoods.model.Product;
import buyinggoods.model.PurchaseRecord;
import buyinggoods.service.PurchaseService;
import buyinggoods.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    RedisUtil<PurchaseRecord> redisUtil = null;

    @Override
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    public boolean purchase(Long userId, Long productId, int quantity) {
        // 当前时间
        long start = System.currentTimeMillis();
        // 循环尝试直至成功
        while(true){
            // 循环时间
            long end = System.currentTimeMillis();
            if(end - start > 100){
                return false;
            }
            // 获取产品
            Product product = productMapper.getProduct(productId);
            // 比较库存和购买数量
            if(product.getStock() < quantity){
                // 库存不足
                return false;
            }
            // 扣减库存
            int result = productMapper.decreaseProduct(productId, quantity, product.getVersion());
            // 更新失败，数据在多线程中被其他线程修改，导致失败返回
            if(result == 0){
                continue;
            }
            // 初始化购买记录
            PurchaseRecord pr = this.iniPurchaseRecord(userId, product, quantity);
            // 插入购买记录
            purchaseRecordMapper.insertPurchaseRecord(pr);
            String s = redisTemplate.opsForValue().get("purchase");
            int purchase = Integer.valueOf(s==null?"0":s);
            if(purchase>0){
                redisTemplate.opsForValue().set("purchase",purchase+1+"");
            }else{
                redisTemplate.opsForValue().set("purchase","1");
            }
            redisUtil.setStr("pr",pr);
            log.info("购买成功!");
            return true;
        }
    }

    /**
     * 初始化购买信息
     * @param userId
     * @param product
     * @param quantity
     * @return
     */
    private PurchaseRecord iniPurchaseRecord(Long userId, Product product, int quantity){
        PurchaseRecord pr = new PurchaseRecord();
        pr.setNote("购买日志 ，时间：" + System.currentTimeMillis());
        pr.setPrice(product.getPrice());
        pr.setProductId(product.getId());
        pr.setQuantity(quantity);
        pr.setSum(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        pr.setUserId(userId);
        return pr;
    }

    /* 使用Redis Lua响应请求 */

    @Autowired
    StringRedisTemplate stringRedisTemplate = null;
    String purchaseScript =
            // 先将产品编号保存到集合中
            " redis.call('sadd', KEYS[1], ARGV[2]) \n"
            // 购买列表
            + "local productPurchaseList = KEYS[2]..ARGV[2] \n"
            // 用户编号
            + "local userId = ARGV[1] \n"
            // 产品键
            + "local product = 'product_'..ARGV[2] \n"
            // 购买数量
            + "local quantity = tonumber(ARGV[3]) \n"
            // 当前库存
            + "local stock = tonumber(redis.call('hget', product, 'stock')) \n"
            // 价格
            + "local price = tonumber(redis.call('hget', product, 'price')) \n"
            // 购买时间
            + "local purchase_date = ARGV[4] \n"
            // 库存不足，返回0
            + "if stock < quantity then return 0 end \n"
            // 减库存
            + "stock = stock - quantity \n"
            + "redis.call('hset', product, 'stock', tostring(stock)) \n"
            // 计算价格
            + "local sum = price * quantity \n"
            // 合并 购买记录数据
            + "local purchaseRecord = userId..','..quantity..','"
            + "..sum..','..price..','..purchase_date \n"
            // 将购买记录保存到list里
            + "redis.call('rpush', productPurchaseList, purchaseRecord) \n"
            // 返回成功
            + "return 1 \n";
    // Redis购买记录集合前缀
    private static final String PURCHASE_PRODUCT_LIST = "purchase_list_";
    // 抢购商品集合
    private static final String PRODUCT_SCHEDULE_SET = "product_schedule_set";
    // 32位SHAL编码，第一次执行的时候先让Redis进行缓存脚本返回
    private String shal = null;

    @Override
    public boolean purchaseRedis(Long userId, Long productId, int quantity) {
        long purchaseDate = System.currentTimeMillis();
        Jedis jedis = null;
        try {
            // 获取原始连接
            jedis = (Jedis)stringRedisTemplate.getConnectionFactory().getConnection().getNativeConnection();
            // 如果没有加载过，则先将脚本加载到Redis服务器，让其返回shal
            if(shal == null){
                shal = jedis.scriptLoad(purchaseScript);
            }
            // 执行脚本，返回结果
            Object res = jedis.evalsha(shal, 2, PRODUCT_SCHEDULE_SET, PURCHASE_PRODUCT_LIST,
                    userId + "", productId + "", quantity + "", purchaseDate + "");
            Long result = (Long) res;
            return result == 1;
        }finally{
            // 关闭jedis连接
            if(jedis != null && jedis.isConnected()){
                jedis.close();
            }
        }
    }

    /**
     * 保存购买记录
     * @param prList
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Throwable.class)
    public boolean dealRedisPurchase(List<PurchaseRecord> prList) {
        for(PurchaseRecord pr : prList){
            purchaseRecordMapper.insertPurchaseRecord(pr);
            productMapper.deleteProduct(pr.getProductId(), pr.getQuantity());
        }
        return true;
    }
}
