package org.scu301.remoteserver.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.service.DeviceDataService;
import org.scu301.remoteserver.service.MemoryCacheService;
import org.scu301.remoteserver.vo.AccountDevicesResponse;
import org.scu301.remoteserver.vo.AreaDevicesResponse;
import org.scu301.remoteserver.vo.HouseDevicesResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Aspect
@Component
public class DeviceDataCacheAspect {
    DeviceDataService deviceDataService;
    MemoryCacheService memoryCacheService;

    public DeviceDataCacheAspect(DeviceDataService deviceDataService, MemoryCacheService memoryCacheService) {
        this.deviceDataService = deviceDataService;
        this.memoryCacheService = memoryCacheService;
    }

// 多余的
    @Around("execution(public java.util.Optional<org.scu301.remoteserver.vo.AccountDevicesResponse> org.scu301.remoteserver.service.DeviceDataService.getAccountDevices(..)) && args(id)")
    public Object cacheAccountDevicesResponse(@NotNull ProceedingJoinPoint joinPoint, Integer id) throws Throwable {
        Optional<AccountDevicesResponse> data = (Optional<AccountDevicesResponse>) joinPoint.proceed();
        if (data.isPresent()) {
            memoryCacheService.cache(data.get());
            log.info("Successfully cached AccountDevicesResponse!");
        }
        return data;
    }

    @Around("execution(public java.util.Optional<org.scu301.remoteserver.vo.HouseDevicesResponse> org.scu301.remoteserver.service.DeviceDataService.getHouseDevices(..)) && args(accountId, houseId)")
    public Object cacheHouseDevicesResponse(@NotNull ProceedingJoinPoint joinPoint, Integer accountId, Integer houseId) throws Throwable {
        Optional<HouseDevicesResponse> data = (Optional<HouseDevicesResponse>) joinPoint.proceed();
        if (data.isPresent()) {
            memoryCacheService.cache(data.get(), accountId);
            log.info("Successfully cached HouseDevicesResponse!");
        }
        return data;
    }

    @Around("execution(public java.util.Optional<org.scu301.remoteserver.vo.AreaDevicesResponse> org.scu301.remoteserver.service.DeviceDataService.getAreaDevices(..)) && args(accountId, areaId)")
    public Object cacheData(@NotNull ProceedingJoinPoint joinPoint, int accountId, int areaId) throws Throwable {
        Optional<AreaDevicesResponse> data = (Optional<AreaDevicesResponse>) joinPoint.proceed();
        if (data.isPresent()) {
            memoryCacheService.cache(data.get(), accountId);
            log.info("Successfully cached AreaDevicesResponse!");
        } else {
            log.info("no data found!");
        }
        return data;
    }
}
