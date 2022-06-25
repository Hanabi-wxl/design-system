package edu.dlu.bysj.paper.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Entrust;
import edu.dlu.bysj.base.model.vo.EntrustInfoVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;

/**
 * @author XiangXinGang
 * @date 2021/10/31 8:54
 */
public interface EntrustService extends IService<Entrust> {

    /**
     * 获取被委托人为 consigneeId 的所有题目信息
     *
     * @param consigneeId 被委托人
     * @return List<EntrustInfo>
     */
    TotalPackageVo<EntrustInfoVo> selfEntrusts(Integer consigneeId, Integer year, Integer pageNumber, Integer pageSize);
}
