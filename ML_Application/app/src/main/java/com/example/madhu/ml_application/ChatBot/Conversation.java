package com.example.madhu.ml_application.ChatBot;



import java.util.Date;

/**
 * The Class Conversation is a Java Bean class that represents a single chat
 * conversation message.
 */
public class Conversation
{

	/** The Constant STATUS_SENDING. */
	public String STATUS_SENDING = "Sending";

	/** The Constant STATUS_SENT. */
	public static final String STATUS_SENT = "Sent";

	/** The Constant STATUS_FAILED. */
	public static final String STATUS_FAILED = "Failed";

	/** The msg. */
	private String msg;

	/** The status. */
	private int status = 0;

	/** The date. */
	private Date date;

	/** The sender. */
	private boolean receiver=true;

	private boolean isImg=false;


	public void setReceiver(boolean re)
	{

		this.receiver=re;
	}

	public void isImage(boolean img)
	{

		this.isImg=img;
	}


	public Conversation() {

	}

	public String getStatusSending()
	{
		return STATUS_SENDING;
	}
	/**
	 * Gets the msg.
	 * 
	 * @return the msg
	 */
	public void setStatusSending(String s)
	{
		this.STATUS_SENDING=s;
	}
	public String getMsg()
	{
		return msg;
	}

	/**
	 * Sets the msg.
	 *
	 * @param msg
	 *            the new msg
	 */
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	/**
	 * Checks if is sent.
	 * 
	 * @return true, if is sent
	 */
	public boolean isSent()
	{
		return receiver;
		//return UserList.user.getId().contentEquals(sender);
	}

	public boolean isImgSet()
	{
		return isImg;
	}

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public Date getDate() {

        return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date
	 *            the new date
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}

    /**
     * Gets the sender.
     *
     * @return the sender
     */

	public int getStatus()
	{
		return 10;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the new status
	 */
	public void setStatus(int status)
	{
		this.status = status;
	}




}
