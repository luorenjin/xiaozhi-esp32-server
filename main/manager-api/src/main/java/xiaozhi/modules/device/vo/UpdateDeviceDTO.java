package xiaozhi.modules.device.vo;

import lombok.Data;

@Data
public class UpdateDeviceDTO {
    private String name;
    private String description;
    private String icon;
    private String alias;
    private String agentId;
    private Integer autoUpdate;
}
