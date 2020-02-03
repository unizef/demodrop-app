package com.unizef.login.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the demofiles database table.
 * 
 */
@Entity
@Table(name="demofiles")
@NamedQuery(name="Demofile.findAll", query="SELECT d FROM Demofile d")
public class Demofile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer demofilesid;

	private Integer createdby;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdtime;
	
	private Integer modifiedby;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedtime;

	private String filedesc;

	private String filelocation;

	public String filename;

	private String status;
	
	private String comments;

	public Demofile() {
	}

	

	public Integer getDemofilesid() {
		return demofilesid;
	}



	public void setDemofilesid(Integer demofilesid) {
		this.demofilesid = demofilesid;
	}



	public Integer getCreatedby() {
		return this.createdby;
	}

	public void setCreatedby(Integer createdby) {
		this.createdby = createdby;
	}

	public Date getCreatedtime() {
		return this.createdtime;
	}

	public void setCreatedtime(Date createdtime) {
		this.createdtime = createdtime;
	}

	public String getFiledesc() {
		return this.filedesc;
	}

	public void setFiledesc(String filedesc) {
		this.filedesc = filedesc;
	}

	public String getFilelocation() {
		return this.filelocation;
	}

	public void setFilelocation(String filelocation) {
		this.filelocation = filelocation;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public String getComments() {
		return comments;
	}



	public void setComments(String comments) {
		this.comments = comments;
	}



	public Integer getModifiedby() {
		return modifiedby;
	}



	public void setModifiedby(Integer modifiedby) {
		this.modifiedby = modifiedby;
	}



	public Date getModifiedtime() {
		return modifiedtime;
	}



	public void setModifiedtime(Date modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

}