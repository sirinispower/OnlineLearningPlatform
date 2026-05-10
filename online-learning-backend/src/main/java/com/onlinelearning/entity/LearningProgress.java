package com.onlinelearning.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("learning_progress")
public class LearningProgress {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long courseId;
    private Long videoId;
    private Integer progress;
    private Integer duration;
    private Boolean completed;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
