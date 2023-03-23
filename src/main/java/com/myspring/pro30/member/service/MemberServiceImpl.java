package com.myspring.pro30.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.member.dao.MemberDAO;
import com.myspring.pro30.member.vo.MemberVO;

@Service("memberService")
@Transactional(propagation = Propagation.REQUIRED)
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDAO memberDAO;
	

	



	@Override
	public List listMembers() throws DataAccessException {
		List membersList=null;
		membersList=memberDAO.selectAllMembers();
		return membersList;
	}


	@Override
	public MemberVO login(MemberVO memberVO) throws Exception {
		// TODO Auto-generated method stub
		return memberDAO.loginById(memberVO);
	}

	@Override
	public int addMember(MemberVO memberVO) throws Exception {
		
		
		
		return memberDAO.insertMember(memberVO);
	}
	
	@Override
	public int removeMember(String id) throws Exception {
		return memberDAO.deleteMember(id);
		
	}
	
	@Override
	public int modMember(MemberVO memberVO) throws Exception  {
		return memberDAO.updateMember(memberVO);
		
		
	}



	@Override
	public int findMember(MemberVO memberVO) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
