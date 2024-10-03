package com.example.testlinux.controller;

import com.example.testlinux.dto.SimpleResponseDto;
import com.example.testlinux.dto.YookassaDto;
import com.example.testlinux.service.YooKassaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/open_events")
@RequiredArgsConstructor
/*@CrossOrigin(origins = {
        "185.71.76.0",
        "185.71.77.0",
        "77.75.153.0",
        "77.75.156.11",
        "77.75.156.35",
        "77.75.154.128",
        "2a02:5180::"
})*/
public class YooKassaController {
    private final YooKassaService yooKassaService;

    @PostMapping("/youkassa")
    public ResponseEntity<SimpleResponseDto> handleNotification(@RequestBody YookassaDto yoo, HttpServletRequest request) {
        log.info("handleNotification() yoo; {}", yoo.getId());
        log.info("request.getRemoteAddr(); {}", request.getRemoteAddr());

        if (!yooKassaService.isIpAllowed(request.getRemoteAddr())) {
            log.info("ERROR YOOKASSA request.getRemoteAddr(); {}", request.getRemoteAddr());
            return ResponseEntity.ok(new SimpleResponseDto("ERROR YOOKASSA"));
        }

        return ResponseEntity.ok(new SimpleResponseDto("SUCCESS"));
    }
}

