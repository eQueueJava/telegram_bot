package com.equeue.repository.impl;

import com.equeue.entity.Provider;
import com.equeue.entity.User;
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

    private static final Map<Long, Provider> PROVIDER_MAP = new HashMap<>();
    private Long id = 1L;

    @Override
    public Provider save(Provider provider) {
        if (provider.getId() == null) {
            provider.setId(this.id++);
        }
        PROVIDER_MAP.put(provider.getId(), provider);
        userRepository.findById(provider.getClient().getId()).getProviders().add(provider);
        return PROVIDER_MAP.get(provider.getId());
    }

    @Override
    public List<Provider> findAll() {
        return new ArrayList<>(PROVIDER_MAP.values());
    }

    @Override
    public Provider findById(Long id) {
        return PROVIDER_MAP.get(id);
    }

    @Override
    public Map<Long, Provider> findAllByUser(User user) {
        Map<Long, Provider> providers = new HashMap<>();

        for (Map.Entry<Long, Provider> entry: PROVIDER_MAP.entrySet()) {
            if(entry.getValue().getClient().equals(user)){
                providers.put(entry.getKey(), entry.getValue());
            }
        }
        return providers;
    }

    @Override
    public List<Provider> deleteAllProvidersForUser(User user) {
        List<Provider> providers = new ArrayList<>();
        Map<Long, Provider> providerMap = findAllByUser(user);
        for (Map.Entry<Long, Provider> entry: providerMap.entrySet()) {
            providers.add(PROVIDER_MAP.remove(entry.getKey()));
        }
        return providers;
    }
}
