package com.unizef.login.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unizef.login.model.Demofile;
import com.unizef.login.repository.DemoFilesRepo;

@Service
public class DemoFilesService {

	@Autowired
	private DemoFilesRepo demofilesrepo;
	
	public Demofile getDemofileOnly(Integer fileid) {

		Demofile list = demofilesrepo.getDemoFilesByPK(fileid);

		return list;

	}

	public Demofile saveDemofile(Demofile entity) {

		demofilesrepo.save(entity);

		return entity;

	}
	
	public List<Demofile> getDemofileListByUserId(Integer userid) {

		List<Demofile> list = demofilesrepo.getDemoFilesByUserid(userid);

		return list;

	}

	public List<Demofile> getDemofileList() {

		List<Demofile> list = demofilesrepo.getALLDemoFiles();

		return list;

	}

	public Demofile updateStatusfile(Demofile entity) {

		demofilesrepo.save(entity);

		return entity;

	}

}
