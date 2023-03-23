package com.myspring.pro30.board.controller;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.member.vo.MemberVO;

@Controller("boardController")
public class BoardControllerImpl implements BoardController {

	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
//	private static final String CURR_IMAGE_REPO_PATH="C:\\board\\article_imagefile";
	private static final String ARTICLE_IMAGE_REPO="C:\\board\\article_image";
	
	@Autowired
	private BoardService boardService;

	@Autowired
	private ArticleVO articleVO;

	@Override
	@RequestMapping(value = "/board/listArticles.do", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String viewName=(String)request.getAttribute("viewName");
		List articlesList= boardService.listArticles();
		ModelAndView mav=new ModelAndView(viewName);
		mav.addObject("articlesList",articlesList);
		return mav;
	}

	//한개 이미지 글쓰기
	@Override
	@RequestMapping(value = "/board/addNewArticle.do",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		Map<String, Object> articleMap=new HashMap<String, Object>();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()) {
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			articleMap.put(name,value);
		}
		String imageFileName=upload(multipartRequest);
		HttpSession session=multipartRequest.getSession();
		MemberVO memberVO=(MemberVO)session.getAttribute("member");
		String id=memberVO.getId();
		articleMap.put("parentNO", 0);
		articleMap.put("id", id);
		articleMap.put("imageFileName", imageFileName);
		System.out.println("addNewArticle에서 이미지 이름 확인 : "+imageFileName);
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders =new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html;charset=utf-8");
		
		try {
			int articleNO=boardService.addNewArticle(articleMap);
			if(imageFileName!=null && imageFileName.length()!=0) {
				File srcFile=new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
				File destDir=new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			message="<script>";
			message+="alert('새글을 추가 했습니다.');";
			message+=" location.href='"+multipartRequest.getContextPath()+"/board/listArticles.do';";
			message+="</script>";
			resEnt=new ResponseEntity(message,responseHeaders,HttpStatus.CREATED);
		} catch (Exception e) {
			File srcFile=new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
			srcFile.delete();
			
			message="<script>";
			message+="alert('오류가 발생했습니다. 다시 시도해 주세요');";
			message+=" location.href='"+multipartRequest.getContextPath()+"/board/articleForm.do';";
			message+="</script>";
			resEnt=new ResponseEntity(message,responseHeaders,HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}
	
	//한개 이미지 업로드하기
	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		String imageFileName=null;
		
		Iterator<String> fileNames=multipartRequest.getFileNames();
		while(fileNames.hasNext()) {
			String fileName=fileNames.next();
			MultipartFile mFile=multipartRequest.getFile(fileName);
			imageFileName=mFile.getOriginalFilename();
			File file=new File(ARTICLE_IMAGE_REPO+"\\"+fileName);
			if(mFile.getSize()!=0) {//file null check
				if(!file.exists()) {
					if(file.getParentFile().mkdirs()) {
						file.createNewFile();
					}
				}
				mFile.transferTo(new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName));
			}
		}
		System.out.println("보드컨트롤러 업로드 메서드 :"+imageFileName);
		return imageFileName;	
	}

	//로그인폼, 게이판 글 입력 폼
	@RequestMapping(value = "/board/*Form.do", method =  RequestMethod.GET)
	private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}
	
	//한개의 이미지 보여주기
	@Override
	@RequestMapping(value = "/board/viewArticle.do",method= {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName=(String)request.getAttribute("viewName");		
		articleVO=boardService.viewArticle(articleNO);
		ModelAndView mav=new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("article",articleVO);
		return mav;
		
	}
	
	@Override
	@RequestMapping(value = "/board/modArticle.do",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception{
		
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String,Object> articleMap = new HashMap<String, Object>();
		Enumeration enu=multipartRequest.getParameterNames();
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			articleMap.put(name,value);
		}
		
		String imageFileName= upload(multipartRequest);
		System.out.println("modArticle에서 이미지파일네임:"+imageFileName);
		articleMap.put("imageFileName", imageFileName);
		
		String articleNO=(String)articleMap.get("articleNO");
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	    try {
	       boardService.modArticle(articleMap);
	       if(imageFileName!=null && imageFileName.length()!=0) {
	         File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
	         File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
	         FileUtils.moveFileToDirectory(srcFile, destDir, true);
	         
	         String originalFileName = (String)articleMap.get("originalFileName");
	         File oldFile = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO+"\\"+originalFileName);
	         oldFile.delete();
	       }	
	       message = "<script>";
		   message += " alert('글을 수정했습니다.');";
		   message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
		   message +=" </script>";
	       resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	    }catch(Exception e) {
	      File srcFile = new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName);
	      srcFile.delete();
	      message = "<script>";
		  message += " alert('오류가 발생했습니다.다시 수정해주세요');";
		  message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
		  message +=" </script>";
	      resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	      e.printStackTrace();
	    }
	    return resEnt;
		
	}

	@Override
	@RequestMapping(value = "/board/removeArticle.do", method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO ,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		response.setContentType("text/html;charset=utf-8");
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders=new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		try {
			boardService.removeArticle(articleNO);
			File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
			FileUtils.deleteDirectory(destDir);
			
			message = "<script>";
			message += " alert('글을 삭제했습니다.');";
			message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		       
		}catch(Exception e) {
			message = "<script>";
			message += " alert('작업중 오류가 발생했습니다.다시 시도해 주세요.');";
			message += " location.href='"+request.getContextPath()+"/board/listArticles.do';";
			message +=" </script>";
		    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		    e.printStackTrace();
		}
		return resEnt;
	}
	
}
