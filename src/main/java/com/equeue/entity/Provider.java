package com.equeue.entity;

import java.util.Objects;

public class Provider {

    private Long id;
    private User client;
    private String name;

    public Provider() {
    }

    public Long getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public String getName() {
        return name;
    }

    public Provider setId(Long id) {
        this.id = id;
        return this;
    }

    public Provider setClient(User client) {
        this.client = client;
        return this;
    }

    public Provider setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provider provider = (Provider) o;
        return Objects.equals(id, provider.id) && Objects.equals(client, provider.client) && Objects.equals(name, provider.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, name);
    }

    @Override
    public String toString() {
        return "Provider{" + "\n" +
                "id=" + id + ", \n" +
                "client='" + client.getName() + "' (" + client.getId() + ")" + ", \n" +
                "name='" + name + "'" +
                '}';
    }

}
