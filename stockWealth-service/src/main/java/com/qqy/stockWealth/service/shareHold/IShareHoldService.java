package com.qqy.stockWealth.service.shareHold;

import com.qqy.stockWealth.dal.entity.ShareHoldInfo;

import java.util.List;

public interface IShareHoldService {
    void runStock();
    List<ShareHoldInfo> getShareHoldInfoList();

}
