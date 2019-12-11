package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the MESSAGES database table.
 * 
 */
@Entity
@Table(name="MESSAGES")
@NamedQuery(name="Message.findAll", query="SELECT m FROM Message m")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="Id")
	private int id;

	@Lob
	private String text;
	
	@Column(name="`broadcast`")
	private Boolean broadcast;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="sender")
	private User sender;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="receiver")
	private User receiver;

	public Message() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getUser1() {
		return this.sender;
	}

	public void setUser1(User user1) {
		this.sender = user1;
	}

	public User getUser2() {
		return this.receiver;
	}

	public void setUser2(User user2) {
		this.receiver = user2;
	}
	public Boolean getBroadcast() {
		return this.broadcast;
	}

	public void setBroadcast(Boolean broadcast) {
		this.broadcast = broadcast;
	}

}