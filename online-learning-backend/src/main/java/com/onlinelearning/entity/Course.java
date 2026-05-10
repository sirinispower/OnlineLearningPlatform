package com.onlinelearning.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("courses")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String description;
    private String cover;
    private Long categoryId;
    private Long instructorId;
    private BigDecimal price;
    private Integer status;
    private Integer sort;

    @TableField("view_count")
    private Integer viewCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    @TableField(exist = false)
    private String instructorName;

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private Double rating;

    @TableField(exist = false)
    private Integer chapterCount;
}
