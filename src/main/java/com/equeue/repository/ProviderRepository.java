package com.equeue.repository;

import com.equeue.entity.Provider;
import com.equeue.entity.User;

import java.util.List;
import java.util.Map;

public interface ProviderRepository {

    Provider save(Provider provider);

    List<Provider> findAll();

    Provider findById(Long id);

    Map<Long, Provider> findAllByUser(User user);

    List<Provider> deleteAllProvidersForUser(User user);

    Provider findByName(String name);
}
