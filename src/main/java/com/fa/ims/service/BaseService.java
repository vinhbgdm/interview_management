package com.fa.ims.service;

import com.fa.ims.entity.BaseEntity;
import com.fa.ims.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface BaseService<E extends BaseEntity, ID extends Serializable> {
    E createNew(E entity);

    E update(E entity);

    void physicalDelete(ID id);

    void delete(ID id);

    void delete(E entity);

    Optional<E> findById(ID id);

    List<E> findAll();

    Page<E> findAll(@Nullable Specification<E> spec, Pageable page);

    default Specification<E> undeletedSpec() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false);
    }


    default String searchLikeKeyword(String searchKey) {
        Objects.requireNonNull(searchKey);
        return "%" + searchKey + "%";
    }

    default String searchLikeLowercaseKeyword(String searchKey) {
        Objects.requireNonNull(searchKey);
        return "%" + searchKey.toLowerCase() + "%";
    }

    List<E> findAll(Specification<E> spec);

}
