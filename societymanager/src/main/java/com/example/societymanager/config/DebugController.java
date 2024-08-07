//package com.example.societymanager.config;
//
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//@RequestMapping("/debug")
//public class DebugController {
//
//    @GetMapping("/csrf_token")
//    public ResponseEntity<String> getCsrfToken(HttpServletRequest request) {
//        try {
//            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//            if (csrfToken != null) {
//                return ResponseEntity.ok("CSRF Token: " + csrfToken.getToken());
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CSRF Token not found.");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
//        }
//    }
//}
