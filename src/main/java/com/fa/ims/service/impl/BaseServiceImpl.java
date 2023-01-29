package com.fa.ims.service.impl;

import com.fa.ims.entity.BaseEntity;
import com.fa.ims.repository.BaseRepository;
import com.fa.ims.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BaseServiceImpl<E extends BaseEntity, ID extends Serializable, R extends BaseRepository<E, ID>>
    implements BaseService<E, ID> {

    protected R repository;

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }

    @Override
    public E createNew(E entity) {
        entity.setDeleted(false);
        return repository.save(entity);
    }

    @Override
    public E update(E entity) {
        return repository.save(entity);
    }

    @Override
    public void physicalDelete(ID id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(ID id) {
        E entity = repository.findById(id).orElseThrow(IllegalArgumentException::new);
        delete(entity);
    }

    @Override
    public void delete(E entity) {
        entity.setDeleted(true);
        repository.save(entity);
    }

    @Override
    public Optional<E> findById(ID id) {
        return repository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<E> findAll() {
        return repository.findAllByDeletedFalse();
    }

    @Override
    public Page<E> findAll(Specification<E> spec, Pageable page) {
        return repository.findAll(spec, page);
    }

	@Override
	public List<E> findAll(Specification<E> spec) {
 		return repository.findAll(spec);
	}


}
