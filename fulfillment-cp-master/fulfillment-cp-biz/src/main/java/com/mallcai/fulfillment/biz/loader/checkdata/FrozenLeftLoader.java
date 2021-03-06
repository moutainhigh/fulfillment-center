package com.mallcai.fulfillment.biz.loader.checkdata;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.mallcai.fulfillment.biz.object.context.CheckContext;
import com.mallcai.fulfillment.biz.object.enums.GroupValueEnum;
import com.mallcai.fulfillment.infrastructure.mapper.erp.OmsStandardOrderMapperExtend;
import com.mallcai.fulfillment.infrastructure.object.erp.OmsStandardOrderExtend;
import com.mallcai.manager.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 冻品数据加载器（ERP）
 * @author Liu Yang
 * @date 2019/9/27 2:09 PM
 */
@Component("FrozenLeftLoader")
@Slf4j
@Service
public class FrozenLeftLoader{

  @Resource
  private OmsStandardOrderMapperExtend omsStandardOrderMapperExtend;

  public Map<String, BigDecimal> loadResource(CheckContext checkContext) {
    log.info("TradeLeftLoader LEFT-LOADER-->get index:{}", JSON.toJSONString(checkContext));
    Map<String,BigDecimal> result= Maps.newHashMap();

    String cities=(String)checkContext.getBizParam().get(Constants.PARAM_CITY);
    List<Integer> cityIdList= StringUtils.isEmpty(cities)?null:(StringUtils.equals(cities,Constants.ALL_CITY)?null: Arrays.asList(cities.split(",")).stream().
            map(w->Integer.valueOf(w)).collect(Collectors.toList()));
    List<OmsStandardOrderExtend> omsStandardOrderExtendList=omsStandardOrderMapperExtend.calFrozenTotalNumByCondition(checkContext.getBeginTime(),checkContext.getEndTime(),cityIdList, Integer.valueOf(GroupValueEnum.STORE_FROZEN_PRODUCT.getGroupValue()));
    if(CollectionUtils.isNotEmpty(omsStandardOrderExtendList)){
      omsStandardOrderExtendList.stream().forEach(item->{
        result.put(item.getCityId()+"",BigDecimal.ONE);
      });
    }
    return result;
  }
}
