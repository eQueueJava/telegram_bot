package com.equeue.entity;

import com.equeue.service.HelperService;

import java.sql.Timestamp;
import java.util.Objects;

public class Session {

    private Long id;
    private Provider provider;
    private User customer;
    private Timestamp sessionStart;
    private Timestamp sessionFinish;

    public Session() {
    }

    public Long getId() {
        return id;
    }

    public Provider getProvider() {
        return provider;
    }

    public User getCustomer() {
        return customer;
    }

    public Timestamp getSessionStart() {
        return sessionStart;
    }

    public Timestamp getSessionFinish() {
        return sessionFinish;
    }

    public Session setId(Long id) {
        this.id = id;
        return this;
    }

    public Session setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public Session setCustomer(User customer) {
        this.customer = customer;
        return this;
    }

    public Session setSessionStart(Timestamp sessionStart) {
        this.sessionStart = sessionStart;
        return this;
    }

    public Session setSessionFinish(Timestamp sessionFinish) {
        this.sessionFinish = sessionFinish;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(id, session.id) && Objects.equals(provider, session.provider) && Objects.equals(customer, session.customer) && Objects.equals(sessionStart, session.sessionStart) && Objects.equals(sessionFinish, session.sessionFinish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, provider, customer, sessionStart, sessionFinish);
    }

//    @Override
//    public String toString() {
//        return "Session { " +
//                "id=" + id + ", \n" +
//                "provider=" + provider.getName() + "' (" + provider.getId() + ")" + ", \n" +
//                "customer=" + customer.getName() + "' (" + customer.getId() + ")" + ", \n" +
//                "sessionStart=" + HelperService.timeOf(sessionStart) + ", \n" +
//                "sessionFinish=" + HelperService.timeOf(sessionFinish) +
//                " }";
//    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", provider=" + provider +
                ", customer=" + customer +
                ", sessionStart=" + sessionStart +
                ", sessionFinish=" + sessionFinish +
                '}';
    }

}
