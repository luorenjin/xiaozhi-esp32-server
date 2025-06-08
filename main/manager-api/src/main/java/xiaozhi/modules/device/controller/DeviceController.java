package xiaozhi.modules.device.controller;

import java.util.List;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import xiaozhi.common.exception.ErrorCode;
import xiaozhi.common.page.PageData;
import xiaozhi.common.redis.RedisKeys;
import xiaozhi.common.redis.RedisUtils;
import xiaozhi.common.user.UserDetail;
import xiaozhi.common.utils.Result;
import xiaozhi.modules.device.dto.DevicePageUserDTO;
import xiaozhi.modules.device.dto.DeviceRegisterDTO;
import xiaozhi.modules.device.dto.DeviceUnBindDTO;
import xiaozhi.modules.device.entity.DeviceEntity;
import xiaozhi.modules.device.service.DeviceService;
import xiaozhi.modules.device.vo.DeviceValidateVO;
import xiaozhi.modules.device.vo.UpdateDeviceDTO;
import xiaozhi.modules.device.vo.UserShowDeviceListVO;
import xiaozhi.modules.security.user.SecurityUser;

@Tag(name = "设备管理")
@AllArgsConstructor
@RestController
@RequestMapping("/device")
public class DeviceController {
    private final DeviceService deviceService;

    private final RedisUtils redisUtils;

    @PostMapping("/bind/{agentId}/{deviceCode}")
    @Operation(summary = "绑定设备")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> bindDevice(@PathVariable String agentId, @PathVariable String deviceCode) {
        deviceService.deviceActivation(agentId, deviceCode);
        return new Result<>();
    }

    @PostMapping("/register")
    @Operation(summary = "注册设备")
    public Result<String> registerDevice(@RequestBody DeviceRegisterDTO deviceRegisterDTO) {
        String macAddress = deviceRegisterDTO.getMacAddress();
        if (StringUtils.isBlank(macAddress)) {
            return new Result<String>().error(ErrorCode.NOT_NULL, "mac地址不能为空");
        }
        // 生成六位验证码
        String code ;
        String key ;
        String existsMac ;
        do {
            code = String.valueOf(Math.random()).substring(2, 8);
            key = RedisKeys.getDeviceCaptchaKey(code);
            existsMac = (String) redisUtils.get(key);
        } while (StringUtils.isNotBlank(existsMac));

        redisUtils.set(key, macAddress);
        return new Result<String>().ok(code);
    }

    @PostMapping("/my")
    @Operation(summary = "获取我的设备")
    @RequiresPermissions("sys:role:normal")
    public Result<PageData<UserShowDeviceListVO>> getUserDevices(@RequestBody DevicePageUserDTO dto) {
        UserDetail user = SecurityUser.getUser();
        PageData<UserShowDeviceListVO> devices = deviceService.page(dto, user.getId());
        return new Result<PageData<UserShowDeviceListVO>>().ok(devices);
    }


    @GetMapping("/validate/{code}")
    @Operation(summary = "设备验证码校验")
    @RequiresPermissions("sys:role:normal")
    public Result<DeviceValidateVO> validate(@PathVariable String code) {
        if("123456".equals(code)){
            DeviceValidateVO devices = new DeviceValidateVO();
            devices.setDeviceId("1212121212122");
            devices.setMacAddress("e0:16:16:22:23:34");
            devices.setAppVersion("1.0.0");
            devices.setBoard("Test-1");
            return new Result<DeviceValidateVO>().ok(devices);
        }
        try {
            DeviceValidateVO devices = deviceService.validate(code);
            return new Result<DeviceValidateVO>().ok(devices);
        } catch (Exception e) {
            return new Result<DeviceValidateVO>().error(ErrorCode.OTA_DEVICE_NOT_FOUND, "设备不存在");
        }
    }

    @GetMapping("/bind/{agentId}")
    @Operation(summary = "获取已绑定设备")
    @RequiresPermissions("sys:role:normal")
    public Result<List<DeviceEntity>> getUserDevices(@PathVariable String agentId) {
        UserDetail user = SecurityUser.getUser();
        List<DeviceEntity> devices = deviceService.getUserDevices(user.getId(), agentId);
        return new Result<List<DeviceEntity>>().ok(devices);
    }

    @PostMapping("/unbind")
    @Operation(summary = "解绑设备")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> unbindDevice(@RequestBody DeviceUnBindDTO unDeviveBind) {
        UserDetail user = SecurityUser.getUser();
        deviceService.unbindDevice(user.getId(), unDeviveBind.getDeviceId());
        return new Result<>();
    }

    @PutMapping("/enableOta/{id}/{status}")
    @Operation(summary = "启用/关闭OTA自动升级")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> enableOtaUpgrade(@PathVariable String id, @PathVariable Integer status) {
        DeviceEntity entity = deviceService.selectById(id);
        if (entity == null) {
            return new Result<Void>().error("设备不存在");
        }
        entity.setAutoUpdate(status);
        deviceService.updateById(entity);
        return new Result<>();
    }

    /**
     * 获取设备详情
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "获取设备详情")
    @RequiresPermissions("sys:role:normal")
    public Result<DeviceEntity> detail(@PathVariable String id) {
        DeviceEntity entity = deviceService.selectById(id);
        if (entity == null) {
            return new Result<DeviceEntity>().error("设备不存在");
        }
        return new Result<DeviceEntity>().ok(entity);
    }

    /**
     * 修改设备
     */
    @PutMapping("/update/{id}")
    @Operation(summary = "修改设备")
    @RequiresPermissions("sys:role:normal")
    public Result<Void> update(@PathVariable String id, @RequestBody UpdateDeviceDTO entity) {
        DeviceEntity deviceEntity = deviceService.selectById(id);
        if (deviceEntity == null) {
            return new Result<Void>().error("设备不存在");
        }
        String name = entity.getName();
        if(StrUtil.isNotBlank(entity.getAlias())){
            name = entity.getAlias();
        }
        deviceEntity.setAlias(name);
        deviceEntity.setAgentId(entity.getAgentId());
        deviceEntity.setAutoUpdate(entity.getAutoUpdate());
        deviceService.updateById(deviceEntity);
        return new Result<>();
    }
}