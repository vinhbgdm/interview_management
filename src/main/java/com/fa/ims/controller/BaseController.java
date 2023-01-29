package com.fa.ims.controller;

import com.fa.ims.constant.CommonConstants;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseController {

    protected List<Sort.Order> buildSortByKey(String sort) {
        if (!StringUtils.hasText(sort)) {
            return CommonConstants.DEFAULT_SORT.startsWith("-") ?
                    Collections.singletonList(Sort.Order.desc(CommonConstants.DEFAULT_SORT.substring(1))) :
                    Collections.singletonList(Sort.Order.asc(CommonConstants.DEFAULT_SORT));
        }
        List<Sort.Order> orderList = new ArrayList<>();
        String[] orderArray = sort.split(",");
        for (String order : orderArray) {
            boolean isDesc = order.startsWith("-");
            orderList.add(isDesc ? Sort.Order.desc(order.substring(1)) : Sort.Order.asc(order));
        }
        return orderList;
    }
}
