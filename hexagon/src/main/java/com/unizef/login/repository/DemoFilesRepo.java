package com.unizef.login.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unizef.login.model.Demofile;


@Repository("DemoFilesRepo")
public interface DemoFilesRepo extends JpaRepository<Demofile, Integer>{
	
	@Query(value = "SELECT * FROM demofiles WHERE demofilesid =:demofilesid ", nativeQuery = true)
	Demofile getDemoFilesByPK(@Param("demofilesid") Integer demofilesid);

	@Query(value = "SELECT * FROM demofiles WHERE createdby =:createdby order by createdtime desc", nativeQuery = true)
	List<Demofile> getDemoFilesByUserid(@Param("createdby") Integer createdby);
	
	@Query(value = "SELECT * FROM demofiles order by createdtime desc", nativeQuery = true)
	List<Demofile> getALLDemoFiles();
	
	@Query(value = "update demofiles set status =:status , comments =:comments   where demofilesid =:demofilesid", nativeQuery = true)
	int UpdateStatusForDemoFiles(@Param("status") String status,@Param("comments") String comments,@Param("demofilesid") Integer demofilesid);
	
}
