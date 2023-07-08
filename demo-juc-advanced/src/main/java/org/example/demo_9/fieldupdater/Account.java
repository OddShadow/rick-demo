package org.example.demo_9.fieldupdater;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class Account {
    public Integer id = 888;
    public volatile int money = 300000;
    
    AtomicIntegerFieldUpdater<Account> fieldUpdater
            = AtomicIntegerFieldUpdater.newUpdater(Account.class, "money");
    public void addMoney(Account account) {
        fieldUpdater.getAndIncrement(account);
    }
}