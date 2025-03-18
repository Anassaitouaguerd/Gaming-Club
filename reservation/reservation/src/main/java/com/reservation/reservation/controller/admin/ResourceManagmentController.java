package com.reservation.reservation.controller.admin;

import com.reservation.reservation.dto.resource.ResourceDTO;
import com.reservation.reservation.entity.enums.ResourceStatus;
import com.reservation.reservation.entity.enums.ResourceType;
import com.reservation.reservation.service.interfaces.admin.ResourceManagmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resource")
@AllArgsConstructor

public class ResourceManagmentController {
    private final ResourceManagmentService resourceManagmentService;

    @GetMapping("/all")
    public ResponseEntity<List<ResourceDTO>> getAllResources(){
        return ResponseEntity.ok(resourceManagmentService.getAllResources());
    }

    @GetMapping("/get/byId/{resourceId}")
    public ResponseEntity<ResourceDTO> getResourceById(@PathVariable Long resourceId){
        return ResponseEntity.ok(resourceManagmentService.getResourceById(resourceId));
    }

    @GetMapping("/get/byStatus/{resourceStatus}")
    public ResponseEntity<List<ResourceDTO>> getResourceById(@PathVariable ResourceStatus resourceStatus){
        return ResponseEntity.ok(resourceManagmentService.getResourceByStatus(resourceStatus));
    }

    @GetMapping("/get/byType/{resourceType}")
    public ResponseEntity<List<ResourceDTO>> getResourceByType(@PathVariable ResourceType resourceType){
        return ResponseEntity.ok(resourceManagmentService.getResourceByType(resourceType));
    }

    @GetMapping("/get/byClubId/{clubId}")
    public ResponseEntity<List<ResourceDTO>> getResourceByClubId(@PathVariable Long clubId){
        return ResponseEntity.ok(resourceManagmentService.getResourcesByClubId(clubId));
    }

    @PostMapping("/create")
    public ResponseEntity<ResourceDTO> createResource(@Valid @RequestBody ResourceDTO resourceDTO){
        return ResponseEntity.ok(resourceManagmentService.createResource(resourceDTO));
    }
    
    @PutMapping("/update/{resourceId}")
    public ResponseEntity<ResourceDTO> updateResource(@PathVariable Long resourceId , @Valid @RequestBody ResourceDTO resourceDTO){
        return ResponseEntity.ok(resourceManagmentService.updateResource(resourceId , resourceDTO));
    }

    @DeleteMapping("/delete/{resourcId}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long resourcId){
        resourceManagmentService.deleteResource(resourcId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
