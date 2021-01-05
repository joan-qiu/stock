package com.qqy.stockWealth.dal.mapper;

import com.qqy.stockWealth.dal.entity.ShareHoldInfo;
import com.qqy.stockWealth.dal.entity.ShareHoldInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface ShareHoldInfoMapper {
    long countByExample(ShareHoldInfoExample example);

    @Insert({
        "insert into share_hold_info (id, stock_id, ",
        "stock_no, stock_base_value)",
        "values (#{id,jdbcType=BIGINT}, #{stockId,jdbcType=VARCHAR}, ",
        "#{stockNo,jdbcType=INTEGER}, #{stockBaseValue,jdbcType=DECIMAL})"
    })
    int insert(ShareHoldInfo record);

    int insertSelective(ShareHoldInfo record);

    List<ShareHoldInfo> selectByExample(ShareHoldInfoExample example);

    int updateByExampleSelective(@Param("record") ShareHoldInfo record, @Param("example") ShareHoldInfoExample example);

    int updateByExample(@Param("record") ShareHoldInfo record, @Param("example") ShareHoldInfoExample example);
}