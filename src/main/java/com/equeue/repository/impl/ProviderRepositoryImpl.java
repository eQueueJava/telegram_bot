package com.equeue.repository.impl;

import com.equeue.entity.Provider;
import com.equeue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.equeue.repository.ProviderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProviderRepositoryImpl implements ProviderRepository {

    @Autowired
    UserRepository userRepository;

    Map<Long, Provider> providerMap = new HashMap<>();
    Long id = 1L;

    @Override
    public Provider save(Provider provider) {
        if (provider.getId() == null) {
            provider.setId(this.id++);
        }
        providerMap.put(provider.getId(), provider);
        userRepository.findById(provider.getClient().getId()).getProviders().add(provider);
        return providerMap.get(provider.getId());
    }

    @Override
    public List<Provider> findAll() {
        return new ArrayList<>(providerMap.values());
    }

    @Override
    public Provider findById(Long id) {
        return providerMap.get(id);
    }

}
