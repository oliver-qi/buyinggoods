package buyinggoods.controller;

import buyinggoods.entity.RecordVo;
import buyinggoods.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/test")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/test")
    public ModelAndView testPage(){
        ModelAndView mv = new ModelAndView("test");
        return mv;
    }

    @PostMapping("/purchase")
    public RecordVo purchase(Long userId, Long productId, int quantity){
        boolean success = purchaseService.purchase(userId, productId, quantity);
        String message = success ? "抢购成功" : "抢购失败";
        return new RecordVo(success, message);
    }
    @PostMapping("/purchaseRedis")
    public RecordVo purchaseRedis(Long userId, Long productId, int quantity){
        boolean success = purchaseService.purchaseRedis(userId, productId, quantity);
        String message = success ? "抢购成功" : "抢购失败";
        return new RecordVo(success, message);
    }

}
