package com.reservation.reservation.controller.admin;

import com.reservation.reservation.dto.ClubDTO;
import com.reservation.reservation.service.interfaces.admin.ClubManagmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/club")
@AllArgsConstructor

public class ClubManagmentController {
    private final ClubManagmentService clubManagmentService;

    @GetMapping("/all")
    public ResponseEntity<List<ClubDTO>> getAllClubs(){
        return ResponseEntity.ok(clubManagmentService.getAllClubs());
    }

    @GetMapping("/get/{clubId}")
    public ResponseEntity<ClubDTO> getClubById(@PathVariable Long clubId){
        return ResponseEntity.ok(clubManagmentService.getClubById(clubId));
    }

    @PostMapping("/add")
    public ResponseEntity<ClubDTO> createClub(@Valid @RequestBody ClubDTO clubDTO){
        return ResponseEntity.ok(clubManagmentService.createClub(clubDTO));
    }

    @PutMapping("/update/{clubId}")
    public ResponseEntity<ClubDTO> updateClub(@PathVariable Long clubId , @Valid @RequestBody ClubDTO clubDTO){
        return ResponseEntity.ok(clubManagmentService.updateClub(clubId , clubDTO));
    }

    @DeleteMapping("/delete/{clubId}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long clubId){
        clubManagmentService.deleteClub(clubId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
