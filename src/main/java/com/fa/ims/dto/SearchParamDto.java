package com.fa.ims.dto;

import com.fa.ims.constant.CommonConstants;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Setter
public class SearchParamDto {

    private Integer size;
    private Integer page;
    private String sort;
    private String searchKey;



    public Integer getSize() {
        return Optional.ofNullable(size).orElse(CommonConstants.DEFAULT_SIZE);
    }

    public Integer getPage() {
        return Optional.ofNullable(page).orElse(CommonConstants.DEFAULT_PAGE);
    }

    public String getSort() {
        return Optional.ofNullable(sort).filter(StringUtils::hasText)
                .orElse(CommonConstants.DEFAULT_SORT);
    }

     public String getSearchKey() {
        return searchKey;
    }

}
