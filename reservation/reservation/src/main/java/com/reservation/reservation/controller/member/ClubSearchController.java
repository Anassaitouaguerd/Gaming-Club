package com.reservation.reservation.controller.member;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.service.interfaces.member.ClubSearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/club/search")
public class ClubSearchController {

    private final ClubSearchService clubSearchService;

    @GetMapping("/getByCity/{city}")
    public ResponseEntity<List<ClubDTO>> getAllClubByCity(@PathVariable String city){
        return ResponseEntity.ok(clubSearchService.findClubsByCity(city));
    }
}
