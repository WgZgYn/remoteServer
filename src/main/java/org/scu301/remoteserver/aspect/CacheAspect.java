package org.scu301.remoteserver.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.service.DataBaseReadService;
import org.scu301.remoteserver.service.DataBaseWriteService;
import org.scu301.remoteserver.service.MemoryCacheService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Aspect
@Component
public class CacheAspect {
    private final MemoryCacheService memoryCacheService;
    private final DataBaseReadService dataBaseReadService;
    private final DataBaseWriteService dataBaseWriteService;

    CacheAspect(MemoryCacheService memoryCacheService, DataBaseReadService dataBaseReadService, DataBaseWriteService dataBaseWriteService) {
        this.memoryCacheService = memoryCacheService;
        this.dataBaseReadService = dataBaseReadService;
        this.dataBaseWriteService = dataBaseWriteService;
    }

    // 对读取数据的方法进行拦截
//    @Around("execution(public java.util.Optional<org.scu301.remoteserver.entity.Account> org.scu301.remoteserver.service.DataBaseReadService.getAccount(..)) && args(id)")
    public Object checkCacheAndReadData(ProceedingJoinPoint joinPoint, Integer id) throws Throwable {
        // 首先检查缓存中是否有数据
        Account cachedData = memoryCacheService.getAccount(id);
        if (cachedData != null) {
            // 如果缓存中有数据，则直接返回缓存中的数据
            log.info("Cache hit, returning cached data...");
            return Optional.of(cachedData);
        }

        // 如果缓存中没有数据，执行原方法从数据库中读取数据
        Object result = joinPoint.proceed(); // 继续执行数据库读取方法
        Optional<Account> account = (Optional<Account>) result;

        if (account.isPresent()) {
            // 将查询结果存入缓存
            memoryCacheService.putAccount(account.get());
            log.info("Cache miss, reading from DB and storing in cache...");
        } else {
            log.info("Not in the database");
        }
        return result;
    }

//    @Around("execution(public java.util.Optional<org.scu301.remoteserver.entity.Account> org.scu301.remoteserver.service.DataBaseReadService.getAccount(..)) && args(username)")
    public Object checkCacheAndReadData(ProceedingJoinPoint joinPoint, String username) throws Throwable {
        // 首先检查缓存中是否有数据
        Account cachedData = memoryCacheService.getAccount(username);
        if (cachedData != null) {
            // 如果缓存中有数据，则直接返回缓存中的数据
            log.info("Cache hit, returning cached data...");
            return Optional.of(cachedData);
        }

        // 如果缓存中没有数据，执行原方法从数据库中读取数据
        Object result = joinPoint.proceed(); // 继续执行数据库读取方法
        Optional<Account> account = (Optional<Account>) result;

        if (account.isPresent()) {
            // 将查询结果存入缓存
            memoryCacheService.putAccount(account.get());
            log.info("Cache miss, reading from DB and storing in cache...");
        } else {
            log.info("Not in the database");
        }
        return result;
    }

//    @Around("execution(public java.util.Optional<org.scu301.remoteserver.entity.Account> org.scu301.remoteserver.service.DataBaseWriteService.saveAccount(..)) && args(account)")
    public Object checkCacheAndReadData(ProceedingJoinPoint joinPoint, Account account) throws Throwable {
        Object result = joinPoint.proceed(); // 继续执行数据库读取方法
        // 将查询结果存入缓存
        memoryCacheService.putAccount((Account) result);
        log.info("update cache");
        return result;
    }
}
