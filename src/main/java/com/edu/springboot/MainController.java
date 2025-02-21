package com.edu.springboot;

import com.edu.springboot.friendrequests.dto.FriendRequestDto;
import com.edu.springboot.friendrequests.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final FriendRequestService friendRequestService;

    /** ✅ 홈 화면 */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /** ✅ 게시판 페이지 */
    @GetMapping("/boards.do")
    public String boards() {
        return "boards";
    }

    /** ✅ 친구 요청 목록을 HTML 페이지로 조회 */
    @GetMapping({"/friends/view", "/friends/page"})
    public String friendsPage(@RequestParam(name = "id", defaultValue = "1") Long id, Model model) {
        System.out.println("🔥 요청된 id: " + id);
        List<FriendRequestDto> requests = friendRequestService.getReceivedRequests(id);
        System.out.println("📌 가져온 친구 요청 개수: " + requests.size());

        model.addAttribute("requests", requests);
        return "friendRequests"; 
    }
}
