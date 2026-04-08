package com.example.tasks_mgt_sys.entities.org;

import com.example.tasks_mgt_sys.entities.org.dto.OrgRequestDto;
import com.example.tasks_mgt_sys.entities.org.dto.OrgResponseDto;
import com.example.tasks_mgt_sys.entities.org.service.OrgService;
import com.example.tasks_mgt_sys.utils.ApiResponse;
import com.example.tasks_mgt_sys.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/org")
public class OrgController {

    private final OrgService orgService;
    private final ResponseHandler responseHandler;

    public OrgController(OrgService orgService, ResponseHandler responseHandler) {
        this.orgService = orgService;
        this.responseHandler = responseHandler;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createOrg(@RequestBody OrgRequestDto requestDto) {
        OrgResponseDto response = orgService.createOrg(requestDto);
        //return responseHandler.responseBuilder("Organization Created Successfully", HttpStatus.CREATED, response);
        return new ResponseEntity<>(ApiResponse.success("Organization created successfully", response), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/get-all-users")
    public ResponseEntity<Object> getAllUsers() {
        var response = orgService.viewOrgUsers();
        //return responseHandler.responseBuilder("Users fetched Successfully", HttpStatus.OK, response);
        return new ResponseEntity<>(ApiResponse.success("Users fetched Successfully", response), HttpStatus.OK);
    }
}
//2024-12-31T23:59:59