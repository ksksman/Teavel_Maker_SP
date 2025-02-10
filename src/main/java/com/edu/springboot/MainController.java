package com.edu.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	// 홈 화면 매핑
	@GetMapping("/")
	public String home() {
		return "home";
	}
	
	@GetMapping("/boards.do")
	public String boards(Model model) {
		
		return "boards";
	}
	
}