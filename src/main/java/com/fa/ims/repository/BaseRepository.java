package com.fa.ims.repository;

import com.fa.ims.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity, ID extends Serializable> extends PagingAndSortingRepository<E, ID>
                                                            , JpaSpecificationExecutor<E> {
    Optional<E> findByIdAndDeletedFalse(ID id);
    List<E> findAllByDeletedFalse();


}
