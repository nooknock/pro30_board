package com.myspring.pro30.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.dao.BoardDAO;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;

@Service("boardService")
@Transactional(propagation = Propagation.REQUIRED)
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDAO boardDAO;

	@Override
	public List<ArticleVO> listArticles() throws Exception {

		List<ArticleVO> articlesList = boardDAO.selectAllArticels();

		return articlesList;

	}

	// 단일 이미지 추가하기
	/*
	 * @Override public int addNewArticle(Map articleMap) throws Exception {
	 * 
	 * return boardDAO.insertNewArticle(articleMap); }
	 */

	
	// 여러 이미지 추가하기
	@Override public int addNewArticle(Map articleMap) throws Exception {
	  int articleNO=boardDAO.insertNewArticle(articleMap);
	  articleMap.put("articleNO", articleNO);
	  boardDAO.insertNewImage(articleMap);
	  return articleNO; 
	}
	 

	//한개 이미지 글 보기
	/*
	 * @Override public ArticleVO viewArticle(int articleNO) throws Exception {
	 * 
	 * return boardDAO.selectArticle(articleNO); }
	 */

	@Override
	public void modArticle(Map articleMap) throws Exception {
		boardDAO.updateNewImage(articleMap);
		boardDAO.updateArticle(articleMap);

	}

	@Override
	public void removeArticle(int articleNO) throws Exception {
		boardDAO.deleteArticle(articleNO);

	}
	
	//다중 이미지 파일 글보기
	@Override
	public Map viewArticle(int articleNO)throws Exception{
		Map articleMap = new HashMap();
		ArticleVO articleVO = boardDAO.selectArticle(articleNO);
		List<ImageVO> imageFileList = boardDAO.selectImageFileList(articleNO);
		ImageVO tt=new ImageVO();
		articleMap.put("article", articleVO);
		System.out.println("서비스에서"+articleVO.getContent());
		articleMap.put("imageFileList", imageFileList);
		System.out.println("서비스에서"+tt.getImageFileName());
		return articleMap;
		
	}

}
