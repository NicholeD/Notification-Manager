package com.kenzie.executorservices.ringupdatescheck.checker;

import com.kenzie.executorservices.ringupdatescheck.model.customer.GetCustomerDevicesRequest;
import com.kenzie.executorservices.ringupdatescheck.model.customer.GetCustomerDevicesResponse;
import com.kenzie.executorservices.ringupdatescheck.model.devicecommunication.GetDeviceSystemInfoRequest;
import com.kenzie.executorservices.ringupdatescheck.model.devicecommunication.GetDeviceSystemInfoResponse;
import com.kenzie.executorservices.ringupdatescheck.model.devicecommunication.RingDeviceFirmwareVersion;
import com.kenzie.executorservices.ringupdatescheck.customer.CustomerService;
import com.kenzie.executorservices.ringupdatescheck.devicecommunication.RingDeviceCommunicatorService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility object for checking version status of devices, and updating
 * them if necessary.
 *
 * For instructional purposes, two implementations of the same logic
 * will be created: checkDevicesIteratively, and checkDevicesConcurrently.
 */
public class DeviceChecker {
    private final CustomerService customerService;
    private final RingDeviceCommunicatorService ringDeviceCommunicatorService;

    /**
     * Constructs a DeviceChecker with the provided dependencies.
     *
     * PARTICIPANTS: Do not change the signature of this constructor
     * @param customerService The CustomerService client to use for Customer operations
     * @param ringDeviceCommunicatorService The RingDeviceCommunicatorService client to use for
     *                                      device communication operations
     */
    public DeviceChecker(CustomerService customerService, RingDeviceCommunicatorService ringDeviceCommunicatorService) {
        this.customerService = customerService;
        this.ringDeviceCommunicatorService = ringDeviceCommunicatorService;
    }

    /**
     * Iteratively checks all devices for the given customer.
     * @param customerId The customer to check devices for
     * @param version The firmware version that we want all devices updated to
     * @return The number of devices that were checked
     */
    public int checkDevicesIteratively(final String customerId, RingDeviceFirmwareVersion version) {
        GetCustomerDevicesRequest devicesRequest = getDevicesRequest(customerId);
        GetCustomerDevicesResponse devicesResponse = customerService.getCustomerDevices(devicesRequest);

        List<DeviceCheckTask> devices = devicesResponse.getDeviceIds().stream()
                .map(deviceId -> new DeviceCheckTask(this, deviceId, version))
                .collect(Collectors.toList());
        devices.stream().forEach(deviceCheckTask -> deviceCheckTask.run());
        return devices.size();
    }

    /**
     * Concurrently checks all devices for the given customer.
     * @param customerId The customer to check devices for
     * @param version The firmware version that we want all devices updated to
     * @return The number of devices that were checked
     */
    public int checkDevicesConcurrently(final String customerId, RingDeviceFirmwareVersion version) {


        return 0;
    }

    /**
     * Updates the device to the specified version.
     * @param deviceId The device identifier of the device to update
     * @param version The version the device should be updated to
     */
    public void updateDevice(final String deviceId, final RingDeviceFirmwareVersion version) {
        System.out.println(String.format("[DeviceChecker] Updating device %s to version %s", deviceId, version));

        // PARTICIPANTS: add remaining implementation here in Phase 4
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public RingDeviceCommunicatorService getRingDeviceCommunicatorService() {
        return ringDeviceCommunicatorService;
    }

    public GetCustomerDevicesRequest getDevicesRequest(String customerId) {
        GetCustomerDevicesRequest.Builder devicesRequestBuilder = GetCustomerDevicesRequest.builder().withCustomerId(customerId);
        return devicesRequestBuilder.build();
    }
}
