package com.myspring.pro30.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.board.vo.ArticleVO;

public interface BoardDAO {

	
	public List selectAllArticels() throws Exception;
	public int insertNewArticle(Map articleMap) throws DataAccessException;
	public ArticleVO selectArticle(int articleNO) throws Exception;
	public void updateArticle(Map articleMap) throws Exception;
	public void deleteArticle(int articleNO) throws Exception;
}
