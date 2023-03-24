<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
request.setCharacterEncoding("utf-8");
%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>

function backToList(obj){
	    obj.action="${contextPath}/board/listArticles.do";
	    obj.submit();
     }

	 function fn_enable(obj){
		 document.getElementById("i_title").disabled=false;
		 document.getElementById("i_content").disabled=false;
		 let imgName= document.getElementsByClassName("i_imageFileName");
		 for(i of imgName){
			 i.disabled=false;
		 }
		 document.getElementById("tr_btn_modify").style.display="block";
		 document.getElementById("tr_file_upload").style.display="block";
		 document.getElementById("tr_btn").style.display="none";
	 }

	 function fn_remove_article(url,articleNO){
		 var form = document.createElement("form");
		 form.setAttribute("method", "post");
		 form.setAttribute("action", url);
	     var articleNOInput = document.createElement("input");
	     articleNOInput.setAttribute("type","hidden");
	     articleNOInput.setAttribute("name","articleNO");
	     articleNOInput.setAttribute("value", articleNO);
		 
	     form.appendChild(articleNOInput);
	     document.body.appendChild(form);
	     form.submit();
	 
	 }
	 
	 function fn_modify_article(obj){
		 obj.action="${contextPath}/board/modArticle.do";
		 obj.submit();
	 }

	 function fn_remove_article(url,articleNO){
		 var form = document.createElement("form");
		 form.setAttribute("method", "post");
		 form.setAttribute("action", url);
	     var articleNOInput = document.createElement("input");
	     articleNOInput.setAttribute("type","hidden");
	     articleNOInput.setAttribute("name","articleNO");
	     articleNOInput.setAttribute("value", articleNO);
		 
	     form.appendChild(articleNOInput);
	     document.body.appendChild(form);
	     form.submit();
	 
	 }
	 
	 function readURL(input) {
	     if (input.files && input.files[0]) {
	         var reader = new FileReader();
	         reader.onload = function (e) {
	             $('#preview').attr('src', e.target.result);
	         }
	         reader.readAsDataURL(input.files[0]);
	     }
	 }  
</script>
<meta charset="UTF-8">
<title>글보기</title>

</head>
<body>
	<form name="frmArticle" method="post" action="${contextPath}"
		enctype="multipart/form-data" accept-charset="utf-8">
		<table border="0" align="center">
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">글번호</td>
				<td><input type="text" value="${articleMap.article.articleNO}"
					disabled> <input type="hidden" name="articleNO"
					value="${articleMap.article.articleNO}"></td>

			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">작성자 아이디</td>
				<td><input type="text" value="${articleMap.article.id}"
					name="writer" disabled></td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">제목</td>
				<td><input type="text" value="${articleMap.article.title}"
					name="title" id="i_title" disabled></td>
			</tr>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">내용</td>
				<td><textarea rows="20" cols="60" name="content" id="i_content"
						disabled>${articleMap.article.content}</textarea></td>
			</tr>

			<c:if
				test="${not empty articleMap.imageFileList && articleMap.imageFileList!='null' }">
				<c:forEach var="item" items="${articleMap.imageFileList}"
					varStatus="status">
					<tr>
						<td width="150" align="center" bgcolor="#FF9933" rowspan="2">
							이미지${status.count }</td>
						<td><input type="hidden" name="originalFileName"
							value="${item.imageFileName }" /> <img
							src="${contextPath}/download.do?articleNO=${articleMap.article.articleNO}&imageFileName=${item.imageFileName}"
							id="preview" width="300" height="auto"/><br></td>
					</tr>
					<tr>
						<td><input type="file" name="imageFileName "
							class="i_imageFileName" disabled onchange="readURL(this);" /></td>
					</tr>
				</c:forEach>
			</c:if>

			<%-- <c:choose>
				<c:when
					test="${not empty articleMap.article.imageFileName && articleMap.article.imageFileName!='null' }">

					<tr>
						<td width="150" align="center" bgcolor="#FF9933" rowspan="2">
							이미지</td>
						<td><input type="hidden" name="originalFileName"
							value="${articleMap.article.imageFileName }" /> <img
							src="${contextPath}/download.do?articleNO=${articleMap.article.articleNO}&imageFileName=${articleMap.article.imageFileName}"
							id="preview" /><br></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="file" name="imageFileName "
							id="i_imageFileName" disabled onchange="readURL(this);" /></td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr id="tr_file_upload">
						<td width="150" align="center" bgcolor="#FF9933" rowspan="2">
							이미지</td>
						<td><input type="hidden" name="originalFileName"
							value="${articleMap.article.imageFileName }" /></td>
					</tr>
					<tr>
						<td></td>
						<td><img id="preview" /><br> <input type="file"
							name="imageFileName " id="i_imageFileName" disabled
							onchange="readURL(this);" /></td>
					</tr>
				</c:otherwise>
			</c:choose> --%>
			<tr>
				<td width="150" align="center" bgcolor="#FF9933">등록일자</td>
				<td><input type=text
					value="<fmt:formatDate value="${articleMap.article.writeDate}" />"
					disabled /></td>
			</tr>
			<tr id="tr_btn_modify" align="left">
				<td colspan="2"><input type=button value="수정반영하기"
					onClick="fn_modify_article(frmArticle)"> <input
					type=button value="취소" onClick="backToList(frmArticle)"></td>
			</tr>
			<tr id="tr_btn">
				<td colspan="2" align="center"><c:if
						test="${member.id == articleMap.article.id }">
						<input type=button value="수정하기" onClick="fn_enable(this.form)">
						<input type=button value="삭제하기"
							onClick="fn_remove_article('${contextPath}/board/removeArticle.do', ${articleMap.article.articleNO})">
					</c:if> <input type=button value="리스트로 돌아가기"
					onClick="backToList(this.form)"> <input type=button
					value="답글쓰기"
					onClick="fn_reply_form('${contextPath}/board/replyForm.do', ${articleMap.article.articleNO})">
				</td>
			</tr>
		</table>


	</form>

</body>
</html>