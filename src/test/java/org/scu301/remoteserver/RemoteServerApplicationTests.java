package org.scu301.remoteserver;

import org.junit.jupiter.api.Test;
import org.scu301.remoteserver.dto.AccountDevices;
import org.scu301.remoteserver.entity.Device;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.repository.*;
import org.scu301.remoteserver.service.DeviceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RemoteServerApplicationTests {
	@Autowired
	DeviceRepository deviceRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	AreaRepository areaRepository;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private DeviceControlRepository deviceControlRepository;
    @Autowired
    private DeviceDataService deviceDataService;

	@Test
	void testDeviceRepository() {
		List<Device> devices = deviceRepository.findDevicesByAreaId(4);
		for (Device device : devices) {
			System.out.println(device.getDeviceName());
		}
	}

	@Test
	void testDeviceControlRepository() {
		deviceControlRepository.findAll().forEach(control -> System.out.println(control.getParameter().toString()));
	}

	@Test
	void testAccountRepository() {
		accountRepository.findById(3).get().getMembers().forEach(member -> System.out.println(member.getId()));
	}

	@Test
	void testHouseRepository() {
		House house = houseRepository.findById(2).get();
		house.getAreas().forEach(System.out::println);
	}

	@Test
	void testHouseRepository2() {
		House house = houseRepository.findById(2).get();
		house.getMembers().forEach(member -> System.out.println(member.getId()));
	}

	@Test
	void testMemberRepository() {
		memberRepository.findMembersByAccountId(3).forEach(member -> System.out.println(member.getId()));
	}










	@Test
	void getAllDevicesControl() {
		AccountDevices accountDevices = deviceDataService.getHousesDevices(3).get();
		System.out.println(accountDevices);
	}
}
