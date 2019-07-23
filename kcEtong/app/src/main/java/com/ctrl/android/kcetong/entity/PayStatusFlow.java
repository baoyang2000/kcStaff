/**
 * 
 */
package com.ctrl.android.kcetong.entity;


/**
 * @author Qiu
 *
 */
public class PayStatusFlow {
	
	public PayStatusFlow(){}
	/**
	 * @param mode
	 * 0:无新增数据
	 * 1：存在配送员电话
	 * 2：存在超市订单号
	 */
	public PayStatusFlow(String flow, String date, int...mode){
		this.flow = flow;
		this.date = date;
		if (mode.length==0)
		{
			this.setMode(0);
		}else
		{
			this.setMode(mode[0]);
		}
	}
	
	/**
	 * 用于新版订单状态展示
	 * @param step 当前展示内容步骤序列<用于区分图片展示>
	 * 		1：订单提交 2：商家接单 3：制作配送中 4：已收货 5：订单取消 6：退款中  7：已退款 8:运单号 9：快递电话
	 * @param mode
	 * 0:无新增数据
	 * 1：存在配送员电话
	 * 2：存在超市订单号
	 */
	public PayStatusFlow(int step, String flow, String date, int...mode){
		this.setStep(step);
		this.flow = flow;
		this.date = date;
		if (mode.length==0)
		{
			this.setMode(0);
		}else
		{
			this.setMode(mode[0]);
		}
	}
	/**
	 * 当前展示内容步骤序列<用于区分图片展示>
	 */
	private int step;
	/**
	 * mode
	 */
	private int mode;
	/**
	 * 流程（例如：付款成功，等待商家接单）
	 */
	private String flow;
	/**
	 * 日期时间(例如：2015-01-04 08:23:34)
	 */
	private String date;
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * 获取 #{bare_field_name}
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}
	/**
	 * 设置 #{bare_field_name}
	 * @param mode the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	/**
	 * 获取 #{bare_field_name}
	 * @return the step
	 */
	public int getStep() {
		return step;
	}
	/**
	 * 设置 #{bare_field_name}
	 * @param step the step to set
	 */
	public void setStep(int step) {
		this.step = step;
	}
	
}
