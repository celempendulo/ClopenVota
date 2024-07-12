package io.clopen.vota.api.controller;

import static io.clopen.vota.api.Constants.COOKIE_NAME_VOTER_ID;

import io.clopen.vota.api.model.Voter;
import io.clopen.vota.api.service.VoterService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ua_parser.Parser;

@RestController
public class HomeController {

  private final VoterService voterService;

  @Autowired
  public HomeController(VoterService voterService) {
    this.voterService = voterService;
  }


  @GetMapping("/")
  public ModelAndView getIndexPage(HttpServletRequest request, HttpServletResponse response) {
    val voterIdOptional = getVoterId(request);
    if (voterIdOptional.isPresent()) {
      return new ModelAndView("index");
    }

    val ipAddress = request.getRemoteAddr();
    val userAgent = request.getHeader(HttpHeaders.USER_AGENT);
    val clientInfo = new Parser().parse(userAgent);
    val deviceType = clientInfo.device.family;
    val voterId = UUID.randomUUID().toString();
    voterService.add(new Voter(voterId, ipAddress, deviceType));
    response.addCookie(new Cookie(COOKIE_NAME_VOTER_ID, voterId));
    return new ModelAndView("index");
  }


  private Optional<String> getVoterId(HttpServletRequest request) {
    val cookies = request.getCookies();
    if(cookies == null) {
      return Optional.empty();
    }
    for (val cookie : cookies) {
      if (COOKIE_NAME_VOTER_ID.equals(cookie.getName())) {
        return Optional.of(cookie.getValue());
      }
    }
    return Optional.empty();
  }

}
