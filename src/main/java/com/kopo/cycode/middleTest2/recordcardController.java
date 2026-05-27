package com.kopo.cycode.middleTest2;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * [성적 관리 컨트롤러 (Controller)]
 * 사용자의 HTTP 요청을 처리하고 서비스 계층으로 전달하는 지휘자 역할을 수행합니다.
 */
@Controller
@CrossOrigin(origins = "*") // 타 도메인에서의 AJAX 요청 허용 (CORS 설정)
public class recordcardController {

	@Autowired
	private GradeService gradeService;

	/**
	 * [메인 화면 로드]
	 * GET /record 요청 시 성적 입력 페이지(JSP)를 반환합니다.
	 */
	@RequestMapping(value = "/record", method = RequestMethod.GET)
	public String recordCard() {
		// ViewResolver에 의해 /WEB-INF/views/record_card.jsp 파일이 호출됨
		return "record_card";
	}

	/**
	 * [학생 성적 추가 API]
	 * 프론트엔드로부터 전달받은 JSON 데이터를 DB에 저장합니다.
	 */
	@RequestMapping(value = "/record/add", method = RequestMethod.POST)
	@ResponseBody // 반환값을 뷰 이름이 아닌 HTTP 응답 바디에 직접 작성 (JSON)
	public String addGrade(@RequestBody GradeDTO grade) {
		try {
			gradeService.insertGrade(grade); // 서비스 계층 호출
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error: " + e.getMessage();
		}
	}

	/**
	 * [학생 성적 목록 조회 API]
	 * DB에 등록된 모든 학생의 성적 리스트를 JSON 형식으로 반환합니다.
	 */
	@RequestMapping(value = "/record/list", method = RequestMethod.GET)
	@ResponseBody
	public List<GradeDTO> getGradeList() {
		// 서비스 계층으로부터 리스트를 받아 그대로 반환 (Jackson 라이브러리에 의해 JSON 자동 변환)
		return gradeService.getAllGrades();
	}

	/**
	 * [학생 성적 수정 API]
	 * 수정된 정보를 받아 DB 업데이트를 수행합니다.
	 */
	@RequestMapping(value = "/record/update", method = RequestMethod.POST)
	@ResponseBody
	public String updateGrade(@RequestBody GradeDTO grade) {
		try {
			gradeService.updateGrade(grade);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error: " + e.getMessage();
		}
	}

	/**
	 * [학생 성적 선택 삭제 API]
	 * 하나 이상의 학생 ID 리스트를 받아 일괄 삭제 처리합니다.
	 */
	@RequestMapping(value = "/record/delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteGrades(@RequestBody List<Integer> ids) {
		try {
			gradeService.deleteGrades(ids);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error: " + e.getMessage();
		}
	}

}
