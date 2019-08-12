package buyinggoods.mapper;

import buyinggoods.model.PurchaseRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PurchaseRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insertPurchaseRecord(PurchaseRecord record);

    int insertSelective(PurchaseRecord record);

    PurchaseRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PurchaseRecord record);

    int updateByPrimaryKey(PurchaseRecord record);
}