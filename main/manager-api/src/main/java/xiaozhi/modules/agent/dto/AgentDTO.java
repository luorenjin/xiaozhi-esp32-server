package xiaozhi.modules.agent.dto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 智能体数据传输对象
 * 用于在服务层和控制器层之间传递智能体相关的数据
 */
@Data
@Schema(description = "智能体对象")
public class AgentDTO {
    @Schema(description = "智能体编码", example = "AGT_1234567890")
    private String id;

    @Schema(description = "智能体名称", example = "客服助手")
    private String agentName;

    @Schema(description = "语音合成模型名称", example = "tts_model_01")
    private String ttsModelName;

    @Schema(description = "音色名称", example = "voice_01")
    private String ttsVoiceName;

    @Schema(description = "大语言模型名称", example = "llm_model_01")
    private String llmModelName;

    @Schema(description = "视觉模型名称", example = "vllm_model_01")
    private String vllmModelName;

    @Schema(description = "记忆模型ID", example = "mem_model_01")
    private String memModelId;

    @Schema(description = "角色设定参数", example = "你是一个专业的客服助手，负责回答用户问题并提供帮助")
    private String systemPrompt;

    @Schema(description = "总结记忆", example = "构建可生长的动态记忆网络，在有限空间内保留关键信息的同时，智能维护信息演变轨迹\n" +
            "根据对话记录，总结user的重要信息，以便在未来的对话中提供更个性化的服务")
    private String summaryMemory;

    @Schema(description = "最后连接时间", example = "2024-03-20 10:00:00")
    private Date lastConnectedAt;

    @Schema(description = "设备数量", example = "10")
    private Integer deviceCount;

    @Schema(description = "所属用户ID")
    private Long userId;

    @Schema(description = "智能体编码")
    private String agentCode;

    @Schema(description = "语音识别模型标识")
    private String asrModelId;

    @Schema(description = "语音活动检测标识")
    private String vadModelId;

    @Schema(description = "大语言模型标识")
    private String llmModelId;

    @Schema(description = "VLLM模型标识")
    private String vllmModelId;

    @Schema(description = "语音合成模型标识")
    private String ttsModelId;

    @Schema(description = "音色标识")
    private String ttsVoiceId;

    @Schema(description = "意图模型标识")
    private String intentModelId;

    @Schema(description = "聊天记录配置（0不记录 1仅记录文本 2记录文本和语音）")
    private Integer chatHistoryConf;

    @Schema(description = "语言编码")
    private String langCode;

    @Schema(description = "交互语种")
    private String language;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    private Date updatedAt;
}