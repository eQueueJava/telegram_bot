package com.equeue.repository;

import com.equeue.entity.Provider;

import java.util.List;

public interface ProviderRepository {

    Provider save(Provider provider);

    List<Provider> findAll();

    Provider findById(Long id);

}
