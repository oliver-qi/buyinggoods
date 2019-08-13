package buyinggoods.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelListener extends AnalysisEventListener {
    /**
     * 自定义用于暂时存储data。
     * 可以通过实例获取该值
     */
    private List<Object> datas = new ArrayList<>();

    @Override
    public void invoke(Object object, AnalysisContext context) {
        System.out.print("当前行：" + context.getCurrentRowNum() + "\t");
        System.out.println(object);
        //数据存储到list，供批量处理，或后续自己业务逻辑处理。
        datas.add(object);
        //根据自己业务做处理
        doSomething(object);
    }

    private void doSomething(Object object) {
        //1、入库调用接口
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        datas.clear();//解析结束销毁不用的资源
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }
}
