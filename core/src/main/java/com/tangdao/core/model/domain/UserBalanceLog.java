/**
 * 
 */
package com.tangdao.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年3月11日
 */
@Getter
@Setter
@TableName("paas_user_balance_log")
public class UserBalanceLog extends DataEntity<UserBalanceLog> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String userId; // 用户ID

	private int platformType; // 平台类型：1-短信，2-流量，3-语音
	private Double balance; // 充值额度
	private int paySource; // 充值源,1:订单充值，2：账号额度划拨,3:系统续充,4:消费
	private int payType; // 支付方式，1：账户转账，2:线下转账，3：支付宝，4：微信支付
	private String orderNo; // 订单号
	private String fromUserCode; // 划拨人ID
	private Double price; // 单价
	private Double totalPrice; // 总价
	
	private String status;
	
	private String remarks;

	@TableField(exist = false)
	private String customerName;

	@TableField(exist = false)
	private String username;

	public UserBalanceLog() {
		super();
	}
}
