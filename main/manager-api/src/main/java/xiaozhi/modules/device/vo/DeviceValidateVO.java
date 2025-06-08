package xiaozhi.modules.device.vo;

import lombok.Data;

@Data
public class DeviceValidateVO {
    private String deviceId;
    private String macAddress;
    private String appVersion;
    private String board;

}
