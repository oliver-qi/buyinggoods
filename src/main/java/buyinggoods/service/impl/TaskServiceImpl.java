package buyinggoods.service.impl;

import buyinggoods.model.PurchaseRecord;
import buyinggoods.service.PurchaseService;
import buyinggoods.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate = null;
    @Autowired
    private PurchaseService purchaseService = null;

    private static final String PRODUCT_SCHEDULE_SET = "product_schedule_set";
    private static final String PURCHASE_PRODUCT_LIST = "purchase_product_";
    // 每次取出1000条，避免一次取出消耗太多内存
    private static final int ONE_TIME_SIZE = 1000;

    /**
     * 每次凌晨1点钟开始执行任务
     */
    @Override
//    @Scheduled(cron = "0 0 1 * * ?")
    // 每5秒执行一次
//    @Scheduled(cron = "0/5 * *  * * ?")
    // 下面是用于测试的配置，每分钟执行一次任务
    @Scheduled(fixedRate = 1000 * 60)
    public void purchaseTask() {
        System.out.println("定时任务开始....");
        Set<String> productIdList = stringRedisTemplate.opsForSet().members(PRODUCT_SCHEDULE_SET);
        List<PurchaseRecord> prList = new ArrayList<>();
        for (String productIdStr : productIdList) {
            Long productId = Long.parseLong(productIdStr);
            String purchaseKey = PURCHASE_PRODUCT_LIST + productId;
            BoundListOperations<String, String> ops = stringRedisTemplate.boundListOps(purchaseKey);
            // 计算记录数
            long size = stringRedisTemplate.opsForList().size(purchaseKey);
            Long times = size % ONE_TIME_SIZE == 0 ? size / ONE_TIME_SIZE : size / ONE_TIME_SIZE + 1;
            for (int i = 0; i < times; i++) {
                // 获取之多TIME_SIZE个抢红包信息
                List<String> prlist = null;
                if (i == 0) {
                    prlist = ops.range(i * ONE_TIME_SIZE, (i + 1) * ONE_TIME_SIZE);
                } else {
                    prlist = ops.range(i * ONE_TIME_SIZE + 1, (i + 1) * ONE_TIME_SIZE);
                }
                for (String prStr : prlist) {
                    PurchaseRecord pr = this.createPurchaseRecord(productId, prStr);
                    prList.add(pr);
                }
                try {
                    // 该方法采用新建事务的方式，不会导致全局事务回滚
                    purchaseService.dealRedisPurchase(prList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 清除列表为空，等待重新写入数据
                prList.clear();
            }
            // 删除购买列表
            stringRedisTemplate.delete("purchaseKey");
            // 从商品集合中删除商品
            stringRedisTemplate.opsForSet().remove(PRODUCT_SCHEDULE_SET, productIdStr);
        }
        System.out.println("定时任务结束...");
    }

    private PurchaseRecord createPurchaseRecord(Long productId, String prStr) {
        String[] arr = prStr.split(",");
        Long userId = Long.parseLong(arr[0]);
        int quantity = Integer.parseInt(arr[1]);
        BigDecimal sum = BigDecimal.valueOf(Double.valueOf(arr[2]));
        BigDecimal price = BigDecimal.valueOf(Double.valueOf(arr[3]));
        Long time = Long.parseLong(arr[4]);
        Timestamp purchaseTime = new Timestamp(time);
        PurchaseRecord pr = new PurchaseRecord();
        pr.setProductId(productId);
        pr.setPurchaseDate(purchaseTime);
        pr.setQuantity(quantity);
        pr.setPrice(price);
        pr.setSum(sum);
        pr.setUserId(userId);
        pr.setNote("购买日志，时间：" + purchaseTime.getTime());
        return pr;
    }
}
