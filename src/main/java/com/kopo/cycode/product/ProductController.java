package com.kopo.cycode.product;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductController {
// 1. 테스트에서 썼던 것처럼 서비스를 자동으로 가져옵니다.
	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/getMaxQty", method = RequestMethod.GET)
	// ⚠️ 이 부분을 수정했습니다! required = false 를 추가하여 파라미터가 없어도 통과하게 만듭니다.
	public String getMaxQty(@RequestParam(value = "regionid", required = false) String regionId, Model model) {

		// 이제 regionid가 없으면 에러가 안 나고 null인 채로 이 if문 안으로 들어옵니다.
		if (regionId != null && !regionId.trim().isEmpty()) {
			String result = productService.getMaxQtyByRegion(regionId);
			model.addAttribute("msg", result);
		} else {
			// 주소창에 그냥 쳤을 때(null일 때) 보여줄 기본 문구
			model.addAttribute("msg", "지역 ID를 입력해주세요!!!");
		}

		return "productResult";
	}

	@PostMapping("/getMaxQty")
	@ResponseBody
	public Map<String, Object> getMaxQtyPost(@RequestParam("regionid") String regionid, @RequestParam("productgroup") String productgroup) {

		Map<String, Object> response = new HashMap<>();

		try {
			// 1. 이미지 3번의 서비스를 호출하여 실제 DB 값을 가져옵니다.
			 String result = productService.getMaxQtyByRegion(regionid, productgroup);

			// 2. 응답 데이터 구성
			response.put("regionId", regionid);
			response.put("productGroup", productgroup);
			response.put("status", "success");
			response.put("result", result);

		} catch (Exception e) {
			// 에러 발생 시 상태값 전달
			response.put("status", "error");
			response.put("message", e.getMessage());
		}

		return response;
	}

	@PostMapping("/getMaxQty2")
	@ResponseBody
	public Map<String, Object> getMaxQty(@RequestParam("regionid") String regionid, String productgroup) {

		Map<String, Object> response = new HashMap<>();
//		String productgroup = "PRODUCT51";

		try {
			// 1. 이미지 3번의 서비스를 호출하여 실제 DB 값을 가져옵니다.
			 String result = productService.getMaxQtyByRegion(regionid, productgroup);
//			 int maxQty = 500;

			// 2. 응답 데이터 구성
			response.put("regionId", regionid);
			response.put("productGroup", productgroup);
			response.put("status", "success");
			response.put("result", result);

		} catch (Exception e) {
			// 에러 발생 시 상태값 전달
			response.put("status", "error");
			response.put("message", e.getMessage());
		}

		return response;
	}

}// [컨트롤러]ProductController