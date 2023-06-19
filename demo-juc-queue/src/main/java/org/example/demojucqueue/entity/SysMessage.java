package org.example.demojucqueue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class SysMessage {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField
    private String message;
    
    @TableField
    private LocalDateTime consumeTime;
    
    @TableField
    private Date updateTime;
    
    @TableField
    private Date createTime;
    
}
