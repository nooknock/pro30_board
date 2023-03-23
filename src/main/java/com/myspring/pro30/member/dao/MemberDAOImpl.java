package com.myspring.pro30.member.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.member.vo.MemberVO;

@Repository("mamberDAO")
public class MemberDAOImpl implements MemberDAO {

	@Autowired
	private SqlSession sqlSession;
	
	
	
	@Override
	public List selectAllMembers() throws DataAccessException {
		
		List<MemberVO> membersList=sqlSession.selectList("mapper.member.selectAllMemberList");
		
		return membersList;
	}

	@Override
	public int insertMember(MemberVO memberVO) throws Exception {
		
		int result=sqlSession.insert("mapper.member.insertMember",memberVO);
		System.out.println(result);
		return result;
	}

	@Override
	public int deleteMember(String id) throws Exception {
		int result=sqlSession.insert("mapper.member.deleteMember",id);
		return result;
	}
	
	@Override
	public int updateMember(MemberVO memberVO) {
		int result=sqlSession.update("mapper.member.updateMember",memberVO);
		System.out.println(result);
		return result;
	}

	@Override
	public MemberVO findMember(String id) throws Exception {
		MemberVO memberVO=(MemberVO) sqlSession.selectList("mapper.member.findMember",id);
		return memberVO;
	}
	
	@Override
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException {
		MemberVO vo=sqlSession.selectOne("mapper.member.loginById",memberVO);
		return vo;
	}
	
}
