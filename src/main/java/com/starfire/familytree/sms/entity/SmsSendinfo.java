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

    private String errorContent;

    private String passNo;

    private Integer priority;

    private Boolean receiveFlag;

    private String sendBy;

    private Integer sendStatus;

    private LocalDateTime sendTime;

    private LocalDateTime realSendTime;

    private LocalDateTime reportTime;

    private String serialNo;

    private String serviceCode;

    private String userMobile;

    private String smsContent;

    private Integer smsType;


}
