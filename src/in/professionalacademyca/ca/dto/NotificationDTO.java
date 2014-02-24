package in.professionalacademyca.ca.dto;

public class NotificationDTO {

	String level;
	String batch;
	String notification;
	String notificationdate;
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	public String getNotificationdate() {
		return notificationdate;
	}
	public void setNotificationdate(String notificationdate) {
		this.notificationdate = notificationdate;
	}
	public NotificationDTO(String level, String batch, String notification,
			String notificationdate) {
		super();
		this.level = level;
		this.batch = batch;
		this.notification = notification;
		this.notificationdate = notificationdate;
	}
}
