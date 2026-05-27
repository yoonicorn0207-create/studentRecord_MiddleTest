package com.kopo.cycode.middleTest2;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "*")
public class recordcardController {

	@Autowired
	private GradeService gradeService;

	/* 메인 화면 로드 */
	@RequestMapping(value = "/record", method = RequestMethod.GET)
	public String recordCard() {
		return "record_card";
	}// 메인화면

	/* 학생 추가 API */
	@RequestMapping(value = "/record/add", method = RequestMethod.POST)
	@ResponseBody
	public String addGrade(@RequestBody GradeDTO grade) {
		try {
			gradeService.insertGrade(grade);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error: " + e.getMessage();
		}
	}// 학생 추가 API

	/* 학생 목록 조회 API */
	@RequestMapping(value = "/record/list", method = RequestMethod.GET)
	@ResponseBody
	public List<GradeDTO> getGradeList() {
		return gradeService.getAllGrades();
	}// 학생 목록 조회 API

	/* 학생 수정 API */
	@RequestMapping(value = "/record/update", method = RequestMethod.POST)
	@ResponseBody
	public String updateGrade(@RequestBody GradeDTO grade) {
		try {
			System.out
					.println("Update request received for ID: " + grade.getId() + ", Name: " + grade.getStudentName());
			gradeService.updateGrade(grade);
			return "success";
		} catch (Exception e) {
			System.err.println("Error during update: " + e.getMessage());
			e.printStackTrace();
			return "error: " + e.getMessage();
		}
	}// 학생 수정 API

	/* 학생 삭제 API */
	@RequestMapping(value = "/record/delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteGrades(@RequestBody List<Integer> ids) {
		try {
			System.out.println("Delete request received for IDs: " + ids);
			gradeService.deleteGrades(ids);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error: " + e.getMessage();
		}
	}// 학생 삭제 API

}// recordcardController
