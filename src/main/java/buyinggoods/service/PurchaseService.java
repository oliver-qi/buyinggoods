package buyinggoods.service;

import buyinggoods.model.PurchaseRecord;

import java.util.List;

public interface PurchaseService {

    /**
     * 处理购买业务
     * @param userId
     * @param productId
     * @param quantity
     * @return 成功or失败
     */
    boolean purchase(Long userId, Long productId, int quantity);

    /**
     *使用Redis处理购买业务
     * @param userId
     * @param productId
     * @param quantity
     * @return
     */
    boolean purchaseRedis(Long userId, Long productId, int quantity);

    /**
     * 保存购买记录
     * @param prList
     * @return
     */
    boolean dealRedisPurchase(List<PurchaseRecord> prList);
}
