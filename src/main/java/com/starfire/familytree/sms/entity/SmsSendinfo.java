package com.starfire.familytree.sms.entity;

import com.starfire.familytree.basic.entity.AbstractEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author luzh
 * @since 2021-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bs_sms_sendinfo")
public class SmsSendinfo extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    /**短信发送失败原因**/
    private String errorContent;
    /**通道编码**/
    private String passNo;
    /**发送优先级**/
    private Integer priority;
    /**发送人**/
    private String sendBy;
    /** 短信状态 */
    private Integer sendStatus;
    /** 计划发送时间 */
    private LocalDateTime sendTime;
    /**短信流水号**/
    private String serialNo;
    /**服务码**/
    private String serviceCode;
    /**手机号**/
    private String userMobile;
    /**短信内容**/
    private String smsContent;

}
