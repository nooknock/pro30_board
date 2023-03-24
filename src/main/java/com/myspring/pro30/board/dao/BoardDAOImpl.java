package com.myspring.pro30.board.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;

@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO{

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List selectAllArticels() throws Exception {
		List<ArticleVO> articlesList=sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articlesList;
	}
	

	//다중 이미지 추가하기
	@Override
	public int insertNewArticle(Map articleMap) throws DataAccessException {
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		sqlSession.insert("mapper.board.insertNewArticle",articleMap);
		return articleNO;
	}

	
	private int selectNewArticleNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}

	@Override
	public ArticleVO selectArticle(int articleNO) throws Exception {
		
		return sqlSession.selectOne("mapper.board.selectArticle",articleNO);
	}

	
	@Override
	public void updateArticle(Map articleMap) throws Exception {
		sqlSession.update("mapper.board.updateArticle", articleMap);
		
	}

	@Override
	public void deleteArticle(int articleNO) throws Exception {
		sqlSession.delete("mapper.board.deleteArticle", articleNO);
		
	}

	@Override
	public void insertNewImage(Map articleMap) throws Exception{
		
		List<ImageVO> imageFileList=(ArrayList)articleMap.get("imageFileList");
		System.out.println("삽입 넘버"+articleMap.get("articleNO"));
		int articleNO=(Integer)articleMap.get("articleNO");
		int imageFileNO=selectNewImageFileNO();
		for(ImageVO imageVO : imageFileList){
			imageVO.setImageFileNO(++imageFileNO);
			imageVO.setArticleNO(articleNO);
		}
		
		sqlSession.insert("mapper.board.insertNewImage",imageFileList);
		
	}
	
	@Override
	public void updateNewImage(Map articleMap) throws Exception{
		
		List<ImageVO> imageFileList=(ArrayList)articleMap.get("imageFileList");
		System.out.println("타입"+articleMap.get("articleNO").getClass().getName());
		int articleNO=(Integer)articleMap.get("articleNO");
		int imageFileNO=(Integer) articleMap.get("imageFileNO");
		for(ImageVO imageVO : imageFileList){
			imageVO.setImageFileNO(imageFileNO);
			imageVO.setArticleNO(articleNO);
		}
		
		sqlSession.insert("mapper.board.updateNewImage",imageFileList);
		
	}

	private int selectNewImageFileNO() throws Exception{
		
		return sqlSession.selectOne("mapper.board.selectNewImageFileNO");
	}

	@Override
	public List selectImageFileList(int articleNO) throws Exception{
		
		List<ImageVO> imageFileList = null;
		imageFileList = sqlSession.selectList("mapper.board.selectImageFileList",articleNO);
		return imageFileList;
	}

	
}
