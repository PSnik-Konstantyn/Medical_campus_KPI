package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;

public class Main {

    public static JedisPool jedisPool;

    public static void main(String[] args) throws URISyntaxException {
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (jedisPool != null) {
                jedisPool.close();
            }
        }));
        StartWindow registrationWindow = new StartWindow();

    }
}