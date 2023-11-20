package sg.gov.org.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "events")
public class SessionEntity {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="event_id")
    private long eventId;

    @Column(name="event_name")
    private String eventName;
    
    
    @Column(name="status")
    private String status;
    
    
    @Column(name="updated_date")
	private Date updt;

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdt() {
		return updt;
	}

	public void setUpdt(Date updt) {
		this.updt = updt;
	}
	
	 @PrePersist
	    protected void onCreate() {
	        updt = new Date(); // Set the default value before persisting
	    }
}
