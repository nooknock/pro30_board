package com.myspring.pro30.member.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.member.vo.MemberVO;

public interface MemberDAO {

	public List selectAllMembers() throws DataAccessException;
	public int insertMember(MemberVO memberVO) throws Exception;
	public int deleteMember(String id) throws Exception;
//	public void modMember(MemberVO memberVO) throws Exception;
	public MemberVO findMember(String id) throws Exception;
	public int updateMember(MemberVO memberVO) throws Exception;
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException;
}
